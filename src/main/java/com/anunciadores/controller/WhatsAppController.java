package com.anunciadores.controller;

import com.anunciadores.service.AuditoriaService;
import com.anunciadores.service.WhatsAppService;
import com.anunciadores.service.interfaces.IConfiguracionService;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Conexion del WhatsApp de la iglesia y activacion de los envios automaticos (solo ADMIN).
 */
@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {

    @Autowired
    private WhatsAppService whatsAppService;

    @Autowired
    private IConfiguracionService configuracionService;

    @Autowired
    private AuditoriaService auditoria;

    @GetMapping("/estado")
    public ResponseEntity<Map<String, Object>> estado() {
        Map<String, Object> r = new LinkedHashMap<>(whatsAppService.estado());
        r.put("preferencias", configuracionService.findPreferenciasEnvio());
        return ResponseEntity.ok(r);
    }

    /** Devuelve el QR para vincular el numero desde WhatsApp > Dispositivos vinculados. */
    @PostMapping("/conectar")
    public ResponseEntity<Map<String, Object>> conectar() throws Exception {
        if (!whatsAppService.isConfigurado()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "WhatsApp no está configurado en el servidor");
        }
        return ResponseEntity.ok(whatsAppService.conectar());
    }

    @PutMapping("/preferencias")
    public ResponseEntity<Map<String, Boolean>> guardarPreferencias(@RequestBody Map<String, Boolean> preferencias) {
        configuracionService.savePreferenciasEnvio(preferencias);
        Map<String, Boolean> actuales = configuracionService.findPreferenciasEnvio();
        auditoria.registrar(AuditoriaService.WHATSAPP, "configuracion", null,
                "Envíos automáticos por WhatsApp: " + actuales);
        return ResponseEntity.ok(actuales);
    }

    /** Envia un mensaje de prueba al numero indicado (por ejemplo el del propio administrador). */
    @PostMapping("/prueba")
    public ResponseEntity<String> prueba(@RequestBody Map<String, String> body) {
        String numero = body.get("numero");
        if (WhatsAppService.normalizarNumero(numero, "57") == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Escribe un celular válido, por ejemplo 3001234567");
        }
        boolean ok = whatsAppService.enviarTexto(numero,
                "✅ Mensaje de prueba de Anunciadores de Cristo. Los envíos por WhatsApp funcionan correctamente.");
        if (!ok) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "No se pudo enviar. Revisa que el WhatsApp de la iglesia esté conectado.");
        }
        auditoria.registrar(AuditoriaService.WHATSAPP, "prueba", null, "Mensaje de prueba enviado");
        return ResponseEntity.ok("Mensaje de prueba enviado");
    }
}
