package com.anunciadores.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Envio de mensajes por WhatsApp usando Evolution API (instalada en el mismo VPS).
 *
 * El numero de la iglesia se conecta una sola vez escaneando un QR desde el panel de administracion.
 * Si Evolution no esta configurado o el numero no esta conectado, los envios se omiten sin error.
 */
@Service
public class WhatsAppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhatsAppService.class);

    private final String apiUrl;
    private final String apiKey;
    private final String instancia;
    private final String codigoPais;
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

    public WhatsAppService(@Value("${whatsapp.api-url:}") String apiUrl,
                           @Value("${whatsapp.api-key:}") String apiKey,
                           @Value("${whatsapp.instancia:adc-iglesia}") String instancia,
                           @Value("${whatsapp.codigo-pais:57}") String codigoPais) {
        this.apiUrl = apiUrl == null ? "" : apiUrl.replaceAll("/+$", "");
        this.apiKey = apiKey;
        this.instancia = instancia;
        this.codigoPais = codigoPais;
    }

    public boolean isConfigurado() {
        return !apiUrl.isEmpty() && apiKey != null && !apiKey.isEmpty();
    }

    /** Estado de la conexion: NO_CONFIGURADO, SIN_INSTANCIA, CONECTADO, DESCONECTADO o ERROR. */
    public Map<String, Object> estado() {
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("instancia", instancia);
        if (!isConfigurado()) {
            resultado.put("estado", "NO_CONFIGURADO");
            return resultado;
        }
        try {
            HttpResponse<String> resp = get("/instance/connectionState/" + instancia);
            if (resp.statusCode() == 404) {
                resultado.put("estado", "SIN_INSTANCIA");
                return resultado;
            }
            JsonNode json = mapper.readTree(resp.body());
            String state = json.path("instance").path("state").asText(json.path("state").asText(""));
            resultado.put("estado", "open".equalsIgnoreCase(state) ? "CONECTADO" : "DESCONECTADO");
        } catch (Exception e) {
            LOGGER.warn("No se pudo consultar el estado de WhatsApp: {}", e.getMessage());
            resultado.put("estado", "ERROR");
        }
        return resultado;
    }

    /**
     * Crea la instancia si no existe y devuelve el QR (imagen base64) para vincular el numero.
     */
    public Map<String, Object> conectar() throws IOException, InterruptedException {
        if (!isConfigurado()) {
            throw new IllegalStateException("WhatsApp no está configurado en el servidor");
        }
        HttpResponse<String> resp = get("/instance/connect/" + instancia);
        if (resp.statusCode() == 404) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("instanceName", instancia);
            body.put("integration", "WHATSAPP-BAILEYS");
            body.put("qrcode", true);
            HttpResponse<String> creada = post("/instance/create", body);
            if (creada.statusCode() >= 300) {
                throw new IOException("Evolution respondió " + creada.statusCode() + " al crear la instancia");
            }
            JsonNode json = mapper.readTree(creada.body());
            if (!json.path("qrcode").path("base64").asText("").isEmpty()) {
                return qrDesde(json.path("qrcode"));
            }
            resp = get("/instance/connect/" + instancia);
        }
        // Evolution puede tardar unos segundos en tener el QR listo ({"count":0}): se reintenta
        JsonNode json = mapper.readTree(resp.body());
        for (int intento = 0; intento < 5 && json.path("base64").asText("").isEmpty(); intento++) {
            Thread.sleep(2000);
            json = mapper.readTree(get("/instance/connect/" + instancia).body());
        }
        return qrDesde(json);
    }

    /**
     * Envia un mensaje de texto. Devuelve false (sin lanzar error) si no se pudo enviar.
     */
    public boolean enviarTexto(String telefono, String texto) {
        String numero = normalizarNumero(telefono, codigoPais);
        if (numero == null || !isConfigurado()) {
            return false;
        }
        try {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("number", numero);
            body.put("text", texto);
            HttpResponse<String> resp = post("/message/sendText/" + instancia, body);
            if (resp.statusCode() >= 300) {
                LOGGER.warn("WhatsApp no enviado a {}: HTTP {}", ocultar(numero), resp.statusCode());
                return false;
            }
            return true;
        } catch (Exception e) {
            LOGGER.warn("WhatsApp no enviado a {}: {}", ocultar(numero), e.getMessage());
            return false;
        }
    }

    /**
     * Convierte el celular guardado al formato internacional sin "+": 3001234567 -> 573001234567.
     * Devuelve null si no parece un celular valido.
     */
    public static String normalizarNumero(String telefono, String codigoPais) {
        if (telefono == null) {
            return null;
        }
        String digitos = telefono.replaceAll("\\D", "");
        if (digitos.length() == 10 && digitos.startsWith("3")) {
            return codigoPais + digitos;
        }
        if (digitos.length() == 12 && digitos.startsWith(codigoPais)) {
            return digitos;
        }
        return null;
    }

    private Map<String, Object> qrDesde(JsonNode json) {
        Map<String, Object> resultado = new LinkedHashMap<>();
        String base64 = json.path("base64").asText("");
        resultado.put("qr", base64.isEmpty() ? null : base64);
        resultado.put("codigoEmparejamiento", json.path("pairingCode").isMissingNode() ? null : json.path("pairingCode").asText(null));
        resultado.put("estado", base64.isEmpty() ? estado().get("estado") : "ESPERANDO_QR");
        return resultado;
    }

    private HttpResponse<String> get(String ruta) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder(URI.create(apiUrl + ruta))
                .timeout(Duration.ofSeconds(20))
                .header("apikey", apiKey)
                .GET().build();
        return http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> post(String ruta, Object body) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder(URI.create(apiUrl + ruta))
                .timeout(Duration.ofSeconds(30))
                .header("apikey", apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                .build();
        return http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    private static String ocultar(String numero) {
        return numero.length() > 4 ? "***" + numero.substring(numero.length() - 4) : "***";
    }
}
