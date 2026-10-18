package com.anunciadores.service;

import com.anunciadores.model.PushSuscripcion;
import com.anunciadores.repository.IPushSuscripcionRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import nl.martijndwars.webpush.Encoding;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.apache.http.HttpResponse;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Notificaciones push (Web Push con claves VAPID) a los celulares y navegadores suscritos.
 * Si las claves VAPID no estan configuradas, el envio se omite sin error.
 */
@Service
public class NotificacionPushService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificacionPushService.class);

    private final IPushSuscripcionRepo suscripcionRepo;
    private final String clavePublica;
    private final PushService push;
    private final ObjectMapper mapper = new ObjectMapper();

    public NotificacionPushService(IPushSuscripcionRepo suscripcionRepo,
                                   @Value("${push.vapid.public-key:}") String clavePublica,
                                   @Value("${push.vapid.private-key:}") String clavePrivada,
                                   @Value("${push.vapid.subject:mailto:admin@anunciaig.com}") String sujeto) {
        this.suscripcionRepo = suscripcionRepo;
        this.clavePublica = clavePublica;
        PushService servicio = null;
        if (!clavePublica.isEmpty() && !clavePrivada.isEmpty()) {
            try {
                if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
                    Security.addProvider(new BouncyCastleProvider());
                }
                servicio = new PushService(clavePublica, clavePrivada, sujeto);
            } catch (Exception e) {
                LOGGER.error("Claves VAPID inválidas: las notificaciones push quedan desactivadas", e);
            }
        } else {
            LOGGER.warn("push.vapid.* no configurado: las notificaciones push quedan desactivadas");
        }
        this.push = servicio;
    }

    public boolean isActivo() {
        return push != null;
    }

    public String getClavePublica() {
        return clavePublica;
    }

    @Transactional
    public void suscribir(Integer idPersona, String endpoint, String p256dh, String auth, String userAgent) {
        PushSuscripcion s = suscripcionRepo.findByEndpoint(endpoint).orElseGet(PushSuscripcion::new);
        s.setIdPersona(idPersona);
        s.setEndpoint(endpoint);
        s.setP256dh(p256dh);
        s.setAuth(auth);
        s.setUserAgent(userAgent != null && userAgent.length() > 255 ? userAgent.substring(0, 255) : userAgent);
        if (s.getFechaCreacion() == null) {
            s.setFechaCreacion(new Date());
        }
        suscripcionRepo.save(s);
    }

    @Transactional
    public void desuscribir(Integer idPersona, String endpoint) {
        suscripcionRepo.findByEndpoint(endpoint)
                .filter(s -> s.getIdPersona().equals(idPersona))
                .ifPresent(suscripcionRepo::delete);
    }

    public boolean tieneSuscripcion(Integer idPersona) {
        return !suscripcionRepo.findByIdPersona(idPersona).isEmpty();
    }

    /** Envia la notificacion a todos los dispositivos de la persona, en segundo plano. */
    @Async
    public void enviar(Integer idPersona, String titulo, String cuerpo, String url) {
        if (push == null || idPersona == null) {
            return;
        }
        List<PushSuscripcion> suscripciones = suscripcionRepo.findByIdPersona(idPersona);
        if (suscripciones.isEmpty()) {
            return;
        }
        byte[] payload;
        try {
            Map<String, String> datos = new LinkedHashMap<>();
            datos.put("title", titulo);
            datos.put("body", cuerpo);
            datos.put("url", url != null ? url : "/");
            payload = mapper.writeValueAsString(datos).getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOGGER.error("No se pudo armar la notificación push", e);
            return;
        }
        for (PushSuscripcion s : suscripciones) {
            try {
                Notification n = new Notification(s.getEndpoint(), s.getP256dh(), s.getAuth(), payload);
                HttpResponse resp = push.send(n, Encoding.AES128GCM);
                int status = resp.getStatusLine().getStatusCode();
                if (status == 404 || status == 410) {
                    // El navegador anulo la suscripcion (app desinstalada o permiso retirado)
                    suscripcionRepo.delete(s);
                } else if (status >= 300) {
                    LOGGER.warn("Push rechazado para la persona {}: HTTP {}", idPersona, status);
                }
            } catch (Exception e) {
                LOGGER.warn("No se pudo enviar push a la persona {}: {}", idPersona, e.getMessage());
            }
        }
    }
}
