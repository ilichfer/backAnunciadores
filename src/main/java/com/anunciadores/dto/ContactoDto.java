package com.anunciadores.dto;
import java.util.Date;
public class ContactoDto {
    private Integer id;
    private String nombre;
    private String email;
    private String asunto;
    private String mensaje;
    private Boolean leido;
    private Date fechaCreacion;
    public ContactoDto() {
    }
    public ContactoDto(Integer id, String nombre, String email, String asunto, String mensaje, Boolean leido, Date fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.leido = leido;
        this.fechaCreacion = fechaCreacion;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
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
    public Boolean getLeido() {
        return leido;
    }
    public void setLeido(Boolean leido) {
        this.leido = leido;
    }
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
