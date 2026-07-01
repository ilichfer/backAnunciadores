  package  com.anunciadores.model;
  import com.anunciadores.model.Persona;
  import java.io.Serializable;
  import javax.persistence.Column;
  import javax.persistence.Entity;
  import javax.persistence.GeneratedValue;
  import javax.persistence.GenerationType;
  import javax.persistence.Id;
  import javax.persistence.JoinColumn;
  import javax.persistence.OneToOne;
  import javax.persistence.Table;
  @Entity
  @Table(name = "curso")
  public class Curso
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "idpago")
    private int idPago;
    @Column(name = "nombrecurso")
    private String nombreCurso;
    @Column(name = "fechainicio")
    private String fechaInicio;
    @Column(name = "fechafin")
    private String fechaFin;
    @Column(name = "valortotal")
    private int valorTotal;
    @Column(name = "comentario")
    private Boolean comentario;
    @Column(name = "activo")
    private boolean activo;
    @OneToOne
    @JoinColumn(name = "profesor", referencedColumnName = "id")
    private Persona profesor;
    public Curso() {}
    public int getId() {
return this.id;
    }
    public void setId(int id) {
this.id = id;
    }
    public int getIdPago() {
return this.idPago;
    }
    public void setIdPago(int idPago) {
this.idPago = idPago;
    }
    public String getNombreCurso() {
return this.nombreCurso;
    }
    public void setNombreCurso(String nombreCurso) {
this.nombreCurso = nombreCurso;
    }
    public int getValorTotal() {
return this.valorTotal;
    }
    public void setValorTotal(int valorTotal) {
this.valorTotal = valorTotal;
    }
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
    public boolean isComentario() {
return this.comentario.booleanValue();
    }
    public void setComentario(boolean comentario) {
this.comentario = Boolean.valueOf(comentario);
    }
    public boolean isActivo() {
return this.activo;
    }
    public void setActivo(boolean activo) {
this.activo = activo;
    }
    public Curso(int id, int idPago, String nombreCurso, String fechaInicio, String fechaFin, int valorTotal, boolean comentario, boolean activo) {
this.id = id;
this.idPago = idPago;
this.nombreCurso = nombreCurso;
this.fechaInicio = fechaInicio;
this.fechaFin = fechaFin;
this.valorTotal = valorTotal;
this.comentario = Boolean.valueOf(comentario);
this.activo = activo;
    }
    public Persona getProfesor() {
return this.profesor;
    }
    public void setProfesor(Persona profesor) {
this.profesor = profesor;
    }
  }
