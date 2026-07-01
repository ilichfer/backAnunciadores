package  com.anunciadores.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "inscripciones")
public class Inscripciones
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private int id;
@Column(name = "id_persona")
private int idPersona;
@Column(name = "id_curso")
private int idCurso;
public int getIdPersona() {
return this.idPersona;
}
public void setIdPersona(int idPersona) {
this.idPersona = idPersona;
}
public int getIdCurso() {
return this.idCurso;
}
public void setIdCurso(int idCurso) {
this.idCurso = idCurso;
}
}
