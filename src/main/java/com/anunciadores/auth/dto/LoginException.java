package com.anunciadores.auth.dto;

/**
 * Error de inicio de sesion con un mensaje pensado para mostrarse al usuario.
 */
public class LoginException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LoginException(String message) {
        super(message);
    }
}
