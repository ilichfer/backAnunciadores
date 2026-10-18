package  com.anunciadores.model;
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
@Table(name = "servicio")
public class Servicio
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private int id;
@Column(name = "id_persona")
private int idPersona;
@Column(name = "id_posicion")
private int idPosicion;
@Column(name = "id_ministerios")
private int idMinisterio;
@Column(name = "fecha_servicio")
Date fechaServicio;
@Column(name = "asistencia")
String asistencia;
/** PENDIENTE (null), CONFIRMADO o NO_PUEDE: respuesta del servidor a su turno. */
@Column(name = "confirmacion", length = 20)
private String confirmacion;
@Column(name = "motivo_confirmacion", length = 300)
private String motivoConfirmacion;
@Temporal(TemporalType.TIMESTAMP)
@Column(name = "fecha_confirmacion")
private Date fechaConfirmacion;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public int getIdPersona() {
return this.idPersona;
}
public void setIdPersona(int idPersona) {
this.idPersona = idPersona;
}
public int getIdPosicion() {
return this.idPosicion;
}
public void setIdPosicion(int idPosicion) {
this.idPosicion = idPosicion;
}
public int getIdMinisterio() {
return this.idMinisterio;
}
public void setIdMinisterio(int idMinisterio) {
this.idMinisterio = idMinisterio;
}
public Date getFechaServicio() {
return this.fechaServicio;
}
public void setFechaServicio(Date fechaServicio) {
this.fechaServicio = fechaServicio;
}
public String getAsistencia() {
return this.asistencia;
}
public void setAsistencia(String asistencia) {
this.asistencia = asistencia;
}
public String getConfirmacion() {
return this.confirmacion;
}
public void setConfirmacion(String confirmacion) {
this.confirmacion = confirmacion;
}
public String getMotivoConfirmacion() {
return this.motivoConfirmacion;
}
public void setMotivoConfirmacion(String motivoConfirmacion) {
this.motivoConfirmacion = motivoConfirmacion;
}
public Date getFechaConfirmacion() {
return this.fechaConfirmacion;
}
public void setFechaConfirmacion(Date fechaConfirmacion) {
this.fechaConfirmacion = fechaConfirmacion;
}
}
