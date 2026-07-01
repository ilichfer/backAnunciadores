package  com.anunciadores.dto;
import java.time.LocalDate;
public class ServiceDTO
{
private LocalDate fechaServicio;
private String idPersona;
private String idPosicion;
private String idMinisterio;
public ServiceDTO() {}
public ServiceDTO(LocalDate fechaServicio, String idPersona, String idPosicion, String idMinisterio) {
this.fechaServicio = fechaServicio;
this.idPersona = idPersona;
this.idPosicion = idPosicion;
this.idMinisterio = idMinisterio;
}
public LocalDate getFechaServicio() {
return this.fechaServicio;
}
public void setFechaServicio(LocalDate fechaServicio) {
this.fechaServicio = fechaServicio;
}
public String getIdPersona() {
return this.idPersona;
}
public void setIdPersona(String idPersona) {
this.idPersona = idPersona;
}
public String getIdPosicion() {
return this.idPosicion;
}
public void setIdPosicion(String idPosicion) {
this.idPosicion = idPosicion;
}
public String getIdMinisterio() {
return this.idMinisterio;
}
public void setIdMinisterio(String idMinisterio) {
this.idMinisterio = idMinisterio;
}
}
