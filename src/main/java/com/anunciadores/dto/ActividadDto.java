package  com.anunciadores.dto;
import com.anunciadores.dto.MesaDto;
import java.util.List;
public class ActividadDto
{
private int id;
private String nombreActividad;
private String fecha;
private int cantidadMesas;
private List<MesaDto> mesas;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public String getNombreActividad() {
return this.nombreActividad;
}
public void setNombreActividad(String nombreActividad) {
this.nombreActividad = nombreActividad;
}
public String getFecha() {
return this.fecha;
}
public void setFecha(String fecha) {
this.fecha = fecha;
}
public List<MesaDto> getMesas() {
return this.mesas;
}
public void setMesas(List<MesaDto> mesas) {
this.mesas = mesas;
}
public int getCantidadMesas() {
return this.cantidadMesas;
}
public void setCantidadMesas(int cantidadMesas) {
this.cantidadMesas = cantidadMesas;
}
}
