package  com.anunciadores.auth.dto;
public class reporRequest {
private String fechaInicio;
private String fechaFin;
private Integer idPersona;
public String getFechaInicio() {
return this.fechaInicio;
}
public void setFechaInicio(String fechaInicio) {
this.fechaInicio = fechaInicio;
}
public String getFechaFin() {
return this.fechaFin;
}
public void setFechaFin(String fechaFin) {
this.fechaFin = fechaFin;
}
public Integer getIdPersona() {
return this.idPersona;
}
public void setIdPersona(Integer idPersona) {
this.idPersona = idPersona;
}
}
