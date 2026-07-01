package com.anunciadores.model;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@Table(name = "notificacion")
public class Notificacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "id_persona")
    private Integer idPersona;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "mensaje")
    private String mensaje;
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_servicio")
    private Date fechaServicio;
    @Column(name = "id_ministerio")
    private Integer idMinisterio;
    @Column(name = "ministerio")
    private String ministerio;
    @Column(name = "posicion")
    private String posicion;
    @Column(name = "leida")
    private Boolean leida = false;
    @Column(name = "enviada")
    private Boolean enviada = false;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion = new Date();
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
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public Date getFechaServicio() {
        return fechaServicio;
    }
    public void setFechaServicio(Date fechaServicio) {
        this.fechaServicio = fechaServicio;
    }
    public Integer getIdMinisterio() {
        return idMinisterio;
    }
    public void setIdMinisterio(Integer idMinisterio) {
        this.idMinisterio = idMinisterio;
    }
    public String getMinisterio() {
        return ministerio;
    }
    public void setMinisterio(String ministerio) {
        this.ministerio = ministerio;
    }
    public String getPosicion() {
        return posicion;
    }
    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }
    public Boolean getLeida() {
        return leida;
    }
    public void setLeida(Boolean leida) {
        this.leida = leida;
    }
    public Boolean getEnviada() {
        return enviada;
    }
    public void setEnviada(Boolean enviada) {
        this.enviada = enviada;
    }
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}