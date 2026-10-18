package com.anunciadores.security;

/**
 * Datos del usuario autenticado, obtenidos del token JWT.
 */
public class AuthenticatedUser {

    public static final String ROL_ADMIN = "ADMIN";
    public static final String ROL_SERVIDOR = "SERVIDOR";
    public static final String ROL_USUARIO = "USUARIO";

    private final Integer id;
    private final String cedula;
    private final String rol;

    public AuthenticatedUser(Integer id, String cedula, String rol) {
        this.id = id;
        this.cedula = cedula;
        this.rol = rol;
    }

    public Integer getId() {
        return id;
    }

    public String getCedula() {
        return cedula;
    }

    public String getRol() {
        return rol;
    }

    public boolean isAdmin() {
        return ROL_ADMIN.equals(rol);
    }
}
