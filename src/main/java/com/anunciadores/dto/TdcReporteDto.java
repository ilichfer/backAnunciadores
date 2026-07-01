package  com.anunciadores.dto;
import java.io.Serializable;
public class TdcReporteDto
implements Serializable
{
private static final long serialVersionUID = 1L;
private int idPersona;
private int cantidadEntregados;
private String nombre;
private Double porcentajeCumplimiento;
public int getCantidadEntregados() {
return this.cantidadEntregados;
}
public void setCantidadEntregados(int cantidadEntregados) {
this.cantidadEntregados = cantidadEntregados;
}
public String getNombre() {
return this.nombre;
}
public void setNombre(String nombre) {
this.nombre = nombre;
}
public int getIdPersona() {
return this.idPersona;
}
public void setIdPersona(int idPersona) {
this.idPersona = idPersona;
}
public Double getPorcentajeCumplimiento() {
return this.porcentajeCumplimiento;
}
public void setPorcentajeCumplimiento(Double porcentajeCumplimiento) {
this.porcentajeCumplimiento = porcentajeCumplimiento;
}
}
