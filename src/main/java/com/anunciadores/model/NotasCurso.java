  package  com.anunciadores.model;
  import com.anunciadores.model.Curso;
  import com.anunciadores.model.Persona;
  import com.sun.istack.NotNull;
  import java.io.Serializable;
  import javax.persistence.Basic;
  import javax.persistence.Column;
  import javax.persistence.Entity;
  import javax.persistence.Id;
  import javax.persistence.JoinColumn;
  import javax.persistence.OneToOne;
  import javax.persistence.Table;
  @Entity
  @Table(name = "notas_curso")
  public class NotasCurso
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @NotNull
    private double id;
    @OneToOne
    @JoinColumn(name = "id_curso", referencedColumnName = "id")
    private Curso curso;
    @OneToOne
    @JoinColumn(name = "id_persona", referencedColumnName = "id")
    private Persona persona;
    @Column(name = "nota_maestro")
    private double notaMaestro;
    @Column(name = "nota_asistencia")
    private double notaAsistencia;
    @Column(name = "nota_practica")
    private double notaPractica;
    @Column(name = "nota_examen_final")
    private double notaExamenFinal;
    @Column(name = "nota_final")
    private double notaFinal;
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
    public Persona getPersona() {
return this.persona;
    }
    public void setPersona(Persona persona) {
this.persona = persona;
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
  }
