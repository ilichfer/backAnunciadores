package com.anunciadores.controller;

import com.anunciadores.security.CurrentUser;
import com.anunciadores.service.NotificacionPushService;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Suscripcion del dispositivo a notificaciones push.
 */
@RestController
@RequestMapping("/api/push")
public class PushController {

    @Autowired
    private NotificacionPushService pushService;

    @GetMapping("/configuracion")
    public ResponseEntity<Map<String, Object>> configuracion() {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("activo", pushService.isActivo());
        r.put("clavePublica", pushService.getClavePublica());
        r.put("suscrito", pushService.tieneSuscripcion(CurrentUser.get().getId()));
        return ResponseEntity.ok(r);
    }

    @PostMapping("/suscripcion")
    @SuppressWarnings("unchecked")
    public ResponseEntity<Void> suscribir(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        Object endpoint = body.get("endpoint");
        Object keys = body.get("keys");
        if (!(endpoint instanceof String) || !(keys instanceof Map)
                || !((String) endpoint).startsWith("https://") || ((String) endpoint).length() > 500) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Suscripción inválida");
        }
        Map<String, Object> k = (Map<String, Object>) keys;
        if (!(k.get("p256dh") instanceof String) || !(k.get("auth") instanceof String)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Suscripción inválida");
        }
        pushService.suscribir(CurrentUser.get().getId(), (String) endpoint, (String) k.get("p256dh"),
                (String) k.get("auth"), request.getHeader("User-Agent"));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/suscripcion")
    public ResponseEntity<Void> desuscribir(@RequestBody Map<String, String> body) {
        if (body.get("endpoint") != null) {
            pushService.desuscribir(CurrentUser.get().getId(), body.get("endpoint"));
        }
        return ResponseEntity.noContent().build();
    }
}
