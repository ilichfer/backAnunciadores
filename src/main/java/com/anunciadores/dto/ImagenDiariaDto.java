package  com.anunciadores.dto;
import java.time.LocalDate;
public class ImagenDiariaDto
{
private int id;
private LocalDate fechaCreacion;
private int idPersona;
private String tdc;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public LocalDate getFechaCreacion() {
return this.fechaCreacion;
}
public void setFechaCreacion(LocalDate fechaCreacion) {
this.fechaCreacion = fechaCreacion;
}
public int getIdPersona() {
return this.idPersona;
}
public void setIdPersona(int idPersona) {
this.idPersona = idPersona;
}
public String getTdc() {
return this.tdc;
}
public void setTdc(String tdc) {
this.tdc = tdc;
}
}
