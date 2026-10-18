package com.anunciadores.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Resumen semanal para el pastor (contenido en JSON) generado cada lunes.
 */
@Entity
@Table(name = "resumen_semanal")
public class ResumenSemanal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(name = "semana_inicio", nullable = false, unique = true)
    private Date semanaInicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "semana_fin", nullable = false)
    private Date semanaFin;

    @Lob
    @Column(name = "contenido", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String contenido;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    public Integer getId() {
        return id;
    }

    public Date getSemanaInicio() {
        return semanaInicio;
    }

    public void setSemanaInicio(Date semanaInicio) {
        this.semanaInicio = semanaInicio;
    }

    public Date getSemanaFin() {
        return semanaFin;
    }

    public void setSemanaFin(Date semanaFin) {
        this.semanaFin = semanaFin;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
