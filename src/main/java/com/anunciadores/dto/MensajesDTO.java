package  com.anunciadores.dto;
import com.anunciadores.model.Persona;
import java.io.Serializable;
import java.util.Date;
public class MensajesDTO
implements Serializable
{
private static final long serialVersionUID = 1L;
private int id;
private String descripcion;
private Persona remitente;
private Persona destinatario;
private Date fechaRegistro;
private String fechaMostrar;
private boolean activo;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public String getDescripcion() {
return this.descripcion;
}
public void setDescripcion(String descripcion) {
this.descripcion = descripcion;
}
public Persona getRemitente() {
return this.remitente;
}
public void setRemitente(Persona remitente) {
this.remitente = remitente;
}
public Persona getDestinatario() {
return this.destinatario;
}
public void setDestinatario(Persona destinatario) {
this.destinatario = destinatario;
}
public Date getFechaRegistro() {
return this.fechaRegistro;
}
public void setFechaRegistro(Date fechaRegistro) {
this.fechaRegistro = fechaRegistro;
}
public boolean isActivo() {
return this.activo;
}
public void setActivo(boolean activo) {
this.activo = activo;
}
public String getFechaMostrar() {
return this.fechaMostrar;
}
public void setFechaMostrar(String fechaMostrar) {
this.fechaMostrar = fechaMostrar;
}
}
