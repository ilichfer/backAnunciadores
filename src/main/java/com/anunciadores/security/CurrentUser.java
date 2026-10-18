package com.anunciadores.security;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

/**
 * Acceso al usuario de la peticion actual y validaciones de propiedad de los datos.
 */
public final class CurrentUser {

    private CurrentUser() {
    }

    public static Optional<AuthenticatedUser> optional() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AuthenticatedUser) {
            return Optional.of((AuthenticatedUser) auth.getPrincipal());
        }
        return Optional.empty();
    }

    public static AuthenticatedUser get() {
        return optional().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Debes iniciar sesión"));
    }

    public static boolean isAdmin() {
        return optional().map(AuthenticatedUser::isAdmin).orElse(false);
    }

    /**
     * Permite continuar solo si el usuario consulta sus propios datos o es administrador.
     */
    public static void requireSelfOrAdmin(Integer idPersona) {
        AuthenticatedUser user = get();
        if (user.isAdmin()) {
            return;
        }
        if (idPersona == null || !idPersona.equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tienes permiso para ver o modificar datos de otra persona");
        }
    }
}
