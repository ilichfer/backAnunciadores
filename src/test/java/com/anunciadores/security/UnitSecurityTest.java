package com.anunciadores.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.anunciadores.service.JwtService;
import com.anunciadores.util.ImageValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

class UnitSecurityTest {

    private static final String SECRET = "clave-de-pruebas-con-mas-de-32-caracteres-123";

    @Test
    void jwtGuardaIdRolYCedula() {
        JwtService jwt = new JwtService(SECRET, "anunciadores", 60000);
        Claims claims = jwt.validarToken(jwt.generarToken(42, "123456", "ADMIN", "Ana Paz"));
        assertEquals("123456", claims.getSubject());
        assertEquals(42, claims.get(JwtService.CLAIM_ID, Integer.class));
        assertEquals("ADMIN", claims.get(JwtService.CLAIM_ROL, String.class));
    }

    @Test
    void jwtRechazaSecretoCorto() {
        assertThrows(IllegalStateException.class, () -> new JwtService("corta", "anunciadores", 60000));
    }

    @Test
    void jwtExpirado() {
        JwtService jwt = new JwtService(SECRET, "anunciadores", -1000);
        String token = jwt.generarToken(1, "1", "USUARIO", "x");
        assertThrows(ExpiredJwtException.class, () -> jwt.validarToken(token));
        assertFalse(jwt.esTokenValido(token));
    }

    @Test
    void validadorAceptaImagenesReales() throws Exception {
        byte[] jpg = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0, 0};
        byte[] png = {(byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A, 0};
        assertEquals(ImageValidator.Formato.JPG,
                ImageValidator.validar(new MockMultipartFile("image", "foto.png", "image/png", jpg)));
        assertEquals(ImageValidator.Formato.PNG,
                ImageValidator.validar(new MockMultipartFile("image", "x", "application/octet-stream", png)));
    }

    @Test
    void validadorRechazaHtmlDisfrazado() {
        byte[] html = "<html><script>alert(1)</script></html>".getBytes(StandardCharsets.UTF_8);
        assertThrows(ResponseStatusException.class, () ->
                ImageValidator.validar(new MockMultipartFile("image", "foto.jpg", "image/jpeg", html)));
        byte[] svg = "<svg xmlns=\"http://www.w3.org/2000/svg\"/>".getBytes(StandardCharsets.UTF_8);
        assertThrows(ResponseStatusException.class, () ->
                ImageValidator.validar(new MockMultipartFile("image", "foto.svg", "image/svg+xml", svg)));
        assertThrows(ResponseStatusException.class, () ->
                ImageValidator.validar(new MockMultipartFile("image", "vacia.jpg", "image/jpeg", new byte[0])));
    }

    @Test
    void rateLimiterBloqueaYSeReinicia() {
        RateLimiter limiter = new RateLimiter(2, 15);
        assertTrue(limiter.tryAcquire("k", 2));
        assertTrue(limiter.tryAcquire("k", 2));
        assertFalse(limiter.tryAcquire("k", 2));
        limiter.reset("k");
        assertTrue(limiter.tryAcquire("k", 2));
    }
}
