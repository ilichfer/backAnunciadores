package com.anunciadores.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Genera y valida los tokens JWT de sesion.
 * El secreto, el emisor y la duracion se leen de la configuracion (nunca del codigo).
 */
@Service
public class JwtService {

    public static final String CLAIM_ID = "id";
    public static final String CLAIM_ROL = "rol";
    public static final String CLAIM_NOMBRE = "nombre";

    private final SecretKey key;
    private final String issuer;
    private final long ttlMillis;

    public JwtService(@Value("${security.jwt.secret}") String secret,
                      @Value("${security.jwt.issuer}") String issuer,
                      @Value("${security.jwt.ttlMillis}") long ttlMillis) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException(
                    "security.jwt.secret no esta configurado o tiene menos de 32 caracteres");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.ttlMillis = ttlMillis;
    }

    public String generarToken(Integer idPersona, String cedula, String rol, String nombre) {
        Date ahora = new Date();
        return Jwts.builder()
                .setSubject(cedula)
                .setIssuer(issuer)
                .claim(CLAIM_ID, idPersona)
                .claim(CLAIM_ROL, rol)
                .claim(CLAIM_NOMBRE, nombre)
                .setIssuedAt(ahora)
                .setExpiration(new Date(ahora.getTime() + ttlMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida firma, emisor y expiracion. Lanza JwtException si el token no es valido.
     */
    public Claims validarToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer(issuer)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getCedulaDesdeToken(String token) {
        return validarToken(token).getSubject();
    }

    public boolean esTokenValido(String token) {
        try {
            validarToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
