package com.anunciadores.auth.dto;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Ingresa tu número de cédula")
    private String cedula;

    @NotBlank(message = "Ingresa tu contraseña")
    private String password;

    public String getCedula() {
        return this.cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
