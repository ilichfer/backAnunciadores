package  com.anunciadores.dto;
public class ReportePagoDto
{
private int id;
private String nombreCurso;
private String persona;
private int valorTotal;
private int pago;
private int deuda;
public int getIdCurso() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public int getValorTotal() {
return this.valorTotal;
}
public void setValorTotal(int valorTotal) {
this.valorTotal = valorTotal;
}
public String getNombreCurso() {
return this.nombreCurso;
}
public void setNombreCurso(String nombreCurso) {
this.nombreCurso = nombreCurso;
}
public String getPersona() {
return this.persona;
}
public void setPersona(String persona) {
this.persona = persona;
}
public int getPago() {
return this.pago;
}
public void setPago(int pago) {
this.pago = pago;
}
public int getDeuda() {
return this.deuda;
}
public void setDeuda(int deuda) {
this.deuda = deuda;
}
public ReportePagoDto(int id, String nombreCurso, String persona, int valorTotal, int pago, int deuda) {
this.id = id;
this.nombreCurso = nombreCurso;
this.persona = persona;
this.valorTotal = valorTotal;
this.pago = pago;
this.deuda = deuda;
}
public ReportePagoDto() {}
}
