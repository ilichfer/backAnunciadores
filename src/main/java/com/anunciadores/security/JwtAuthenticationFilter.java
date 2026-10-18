package com.anunciadores.security;

import com.anunciadores.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Lee el header "Authorization: Bearer ..." y, si el token es valido, deja al usuario autenticado.
 * Si el token falta o no es valido la peticion sigue sin usuario y Spring Security decide
 * (las rutas publicas funcionan, las protegidas responden 401).
 *
 * No se registra como @Component para que no se ejecute tambien fuera de la cadena de seguridad.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(BEARER)) {
            try {
                Claims claims = jwtService.validarToken(header.substring(BEARER.length()));
                Integer id = claims.get(JwtService.CLAIM_ID, Integer.class);
                String rol = claims.get(JwtService.CLAIM_ROL, String.class);
                if (id != null && rol != null) {
                    AuthenticatedUser user = new AuthenticatedUser(id, claims.getSubject(), rol);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            user, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol)));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (JwtException | IllegalArgumentException e) {
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
