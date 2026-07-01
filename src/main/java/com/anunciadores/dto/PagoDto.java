package  com.anunciadores.dto;
public class PagoDto
{
private int idPersona;
private int idCurso;
private int valorTotal;
private int valorPagado;
public int getIdCurso() {
return this.idCurso;
}
public void setIdCurso(int idCurso) {
this.idCurso = idCurso;
}
public int getValorTotal() {
return this.valorTotal;
}
public void setValorTotal(int valorTotal) {
this.valorTotal = valorTotal;
}
public int getValorPagado() {
return this.valorPagado;
}
public void setValorPagado(int valorPagado) {
this.valorPagado = valorPagado;
}
public int getIdPersona() {
return this.idPersona;
}
public void setIdPersona(int idPersona) {
this.idPersona = idPersona;
}
public PagoDto(int idPersona, int idCurso, int valorTotal, int valorPagado) {
this.idPersona = idPersona;
this.idCurso = idCurso;
this.valorTotal = valorTotal;
this.valorPagado = valorPagado;
}
}
