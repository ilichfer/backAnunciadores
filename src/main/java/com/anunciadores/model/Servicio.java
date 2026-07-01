package  com.anunciadores.model;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
}
