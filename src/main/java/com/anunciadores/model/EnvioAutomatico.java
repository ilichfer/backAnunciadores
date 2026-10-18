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
import javax.persistence.UniqueConstraint;

/**
 * Marca que un envio automatico (saludo, recordatorio, resumen) ya se hizo para una persona en
 * una fecha, para no repetirlo si el servidor se reinicia o la tarea corre dos veces.
 */
@Entity
@Table(name = "envio_automatico",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tipo", "id_persona", "fecha"}))
public class EnvioAutomatico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tipo", length = 40, nullable = false)
    private String tipo;

    @Column(name = "id_persona", nullable = false)
    private Integer idPersona;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha", nullable = false)
    private Date fecha;

    @Column(name = "canales", length = 60)
    private String canales;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_envio")
    private Date fechaEnvio;

    public EnvioAutomatico() {
    }

    public EnvioAutomatico(String tipo, Integer idPersona, Date fecha) {
        this.tipo = tipo;
        this.idPersona = idPersona;
        this.fecha = fecha;
        this.fechaEnvio = new Date();
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public Integer getIdPersona() {
        return idPersona;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getCanales() {
        return canales;
    }

    public void setCanales(String canales) {
        this.canales = canales;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }
}
