package com.anunciadores.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Dispositivo (navegador o app instalada) suscrito a notificaciones push de una persona.
 */
@Entity
@Table(name = "push_suscripcion")
public class PushSuscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_persona", nullable = false)
    private Integer idPersona;

    @Column(name = "endpoint", length = 500, nullable = false, unique = true)
    private String endpoint;

    @Column(name = "p256dh", length = 200, nullable = false)
    private String p256dh;

    @Column(name = "auth", length = 100, nullable = false)
    private String auth;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getP256dh() {
        return p256dh;
    }

    public void setP256dh(String p256dh) {
        this.p256dh = p256dh;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
