package  com.anunciadores.dto;
import com.anunciadores.model.Curso;
import java.io.Serializable;
public class NotasCursoDTO
implements Serializable
{
private static final long serialVersionUID = 1L;
private double id;
private Curso curso;
private double notaMaestro;
private double notaAsistencia;
private double notaPractica;
private double notaExamenFinal;
private double notaFinal;
private int colorCelda;
public double getId() {
return this.id;
}
public void setId(double id) {
this.id = id;
}
public Curso getCurso() {
return this.curso;
}
public void setCurso(Curso curso) {
this.curso = curso;
}
public double getNotaMaestro() {
return this.notaMaestro;
}
public void setNotaMaestro(double notaMaestro) {
this.notaMaestro = notaMaestro;
}
public double getNotaAsistencia() {
return this.notaAsistencia;
}
public void setNotaAsistencia(double notaAsistencia) {
this.notaAsistencia = notaAsistencia;
}
public double getNotaPractica() {
return this.notaPractica;
}
public void setNotaPractica(double notaPractica) {
this.notaPractica = notaPractica;
}
public double getNotaExamenFinal() {
return this.notaExamenFinal;
}
public void setNotaExamenFinal(double notaExamenFinal) {
this.notaExamenFinal = notaExamenFinal;
}
public double getNotaFinal() {
return this.notaFinal;
}
public void setNotaFinal(double notaFinal) {
this.notaFinal = notaFinal;
}
public int getColorCelda() {
return this.colorCelda;
}
public void setColorCelda(int colorCelda) {
this.colorCelda = colorCelda;
}
}
