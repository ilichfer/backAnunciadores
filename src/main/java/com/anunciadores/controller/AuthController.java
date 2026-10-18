package com.anunciadores.controller;

import com.anunciadores.auth.dto.LoginException;
import com.anunciadores.auth.dto.LoginRequest;
import com.anunciadores.security.RateLimiter;
import com.anunciadores.service.AuthService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /** Limite de intentos fallidos por IP (sumando todas las cedulas): cubre redes compartidas. */
    private static final int MAX_FALLOS_POR_IP = 50;

    @Autowired
    private AuthService authService;

    @Autowired
    private RateLimiter rateLimiter;

    @PostMapping("/loginReact")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest http) {
        String ip = RateLimiter.clientIp(http);
        String claveCuenta = "login:" + ip + ":" + request.getCedula().trim();
        String claveIp = "login-ip:" + ip;

        if (rateLimiter.isBlocked(claveCuenta) || rateLimiter.isBlocked(claveIp, MAX_FALLOS_POR_IP)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Map.of("message",
                    "Demasiados intentos fallidos. Espera 15 minutos o usa \"¿Olvidaste tu contraseña?\"."));
        }
        try {
            Object response = this.authService.login(request);
            rateLimiter.reset(claveCuenta);
            return ResponseEntity.ok(response);
        } catch (LoginException e) {
            rateLimiter.record(claveCuenta);
            rateLimiter.record(claveIp);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        }
    }
}
