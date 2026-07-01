package  com.anunciadores.dto;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;
public class ServicioDto
implements Serializable
{
private static final long serialVersionUID = 1L;
private List<String> posicion;
private List<String> encargado;
private List<String> asistencia;
private Date fechaServcio;
public List<String> getPosicion() {
return this.posicion;
}
public void setPosicion(List<String> posicion) {
this.posicion = posicion;
}
public List<String> getEncargado() {
return this.encargado;
}
public void setEncargado(List<String> encargado) {
this.encargado = encargado;
}
public Date getFechaServcio() {
return this.fechaServcio;
}
public void setFechaServcio(Date fechaServcio) {
this.fechaServcio = fechaServcio;
}
public List<String> getAsistencia() {
return this.asistencia;
}
public void setAsistencia(List<String> asistencia) {
this.asistencia = asistencia;
}
}
