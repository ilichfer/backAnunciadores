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
 * Registro de la bitacora: quien hizo que cambio y cuando.
 */
@Entity
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha", nullable = false)
    private Date fecha;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "usuario", length = 150)
    private String usuario;

    @Column(name = "accion", length = 50, nullable = false)
    private String accion;

    @Column(name = "entidad", length = 50)
    private String entidad;

    @Column(name = "id_entidad", length = 50)
    private String idEntidad;

    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    @Column(name = "ip", length = 45)
    private String ip;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(String idEntidad) {
        this.idEntidad = idEntidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
