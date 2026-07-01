package  com.anunciadores.dto;
import com.anunciadores.model.Persona;
import java.io.Serializable;
import java.sql.Date;
public class CoordinadorDTO
implements Serializable
{
private static final long serialVersionUID = 1L;
private int id;
private int idPersona;
private Date fechaServcio;
private String fechaString;
public Date fechaServicio;
public String notasServicio;
private Persona persona;
public int getIdPersona() {
return this.idPersona;
}
public void setIdPersona(int idPersona) {
this.idPersona = idPersona;
}
public Date getFechaServcio() {
return this.fechaServcio;
}
public void setFechaServcio(Date fechaServcio) {
this.fechaServcio = fechaServcio;
}
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public String getFechaString() {
return this.fechaString;
}
public void setFechaString(String fechaString) {
this.fechaString = fechaString;
}
public Date getFechaServicio() {
return this.fechaServicio;
}
public void setFechaServicio(Date fechaServicio) {
this.fechaServicio = fechaServicio;
}
public String getNotasServicio() {
return this.notasServicio;
}
public void setNotasServicio(String notasServicio) {
this.notasServicio = notasServicio;
}
public Persona getPersona() {
return this.persona;
}
public void setPersona(Persona persona) {
this.persona = persona;
}
}
