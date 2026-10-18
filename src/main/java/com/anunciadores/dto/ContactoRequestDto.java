package com.anunciadores.dto;

import javax.validation.constraints.Size;

public class ContactoRequestDto {
    @Size(max = 100, message = "El nombre es demasiado largo")
    private String nombre;
    @Size(max = 150, message = "El correo es demasiado largo")
    private String email;
    @Size(max = 150, message = "El asunto es demasiado largo")
    private String asunto;
    @Size(max = 2000, message = "El mensaje no puede superar 2000 caracteres")
    private String mensaje;
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAsunto() {
        return asunto;
    }
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
