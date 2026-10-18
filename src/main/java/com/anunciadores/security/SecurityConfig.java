package com.anunciadores.security;

import com.anunciadores.service.JwtService;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Reglas de acceso de la API.
 *
 * Publico:  login, cambio de contrasena por cedula, registro publico, formulario de contacto
 *           e imagen del mes (landing).
 * ADMIN:    gestion de usuarios, roles, ministerios, programacion, cursos, configuracion,
 *           mensajes de contacto, reportes e imagen del mes.
 * Resto de /api/**: cualquier usuario con sesion. Los datos personales se validan ademas
 *           en cada endpoint (solo los propios o ADMIN), ver CurrentUser.requireSelfOrAdmin.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String ADMIN = AuthenticatedUser.ROL_ADMIN;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, ex) ->
                        writeJson(response, HttpServletResponse.SC_UNAUTHORIZED,
                                "Tu sesión expiró o no has iniciado sesión"))
                .accessDeniedHandler((request, response, ex) ->
                        writeJson(response, HttpServletResponse.SC_FORBIDDEN,
                                "No tienes permisos para realizar esta acción"))
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/error").permitAll()
                // Publicos
                .antMatchers(HttpMethod.POST, "/api/auth/loginReact", "/api/updatePassword",
                        "/api/personas/register", "/api/contacto").permitAll()
                .antMatchers(HttpMethod.GET, "/api/imagen-mensual/*").permitAll()
                // Solo administradores
                .antMatchers("/api/personas/todas", "/api/personas/*/rol", "/api/personas/*/toggle-active").hasRole(ADMIN)
                .antMatchers("/api/buscar/**", "/api/consutarEmail", "/api/consutarDoc").hasRole(ADMIN)
                .antMatchers("/api/contacto", "/api/contacto/**").hasRole(ADMIN)
                .antMatchers(HttpMethod.POST, "/api/imagen-mensual/upload").hasRole(ADMIN)
                .antMatchers("/api/cursos", "/api/cursos/**").hasRole(ADMIN)
                .antMatchers(HttpMethod.PUT, "/api/configuracion/**").hasRole(ADMIN)
                .antMatchers("/api/ministries/addposition", "/api/ministeries/**").hasRole(ADMIN)
                .antMatchers("/api/savecordinador", "/api/saveService", "/api/updateprog",
                        "/api/scheduleByDate", "/api/findprog").hasRole(ADMIN)
                // Cualquier usuario con sesion
                .antMatchers("/api/**").authenticated()
                .anyRequest().denyAll()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${security.cors.allowed-origins}") List<String> allowedOrigins) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private static void writeJson(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\":\"" + message + "\"}");
    }
}
