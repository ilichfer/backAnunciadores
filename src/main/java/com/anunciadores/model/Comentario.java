package  com.anunciadores.model;
import com.sun.istack.NotNull;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "comentario")
public class Comentario
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@Basic(optional = false)
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
@NotNull
private int id;
@Column(name = "id_curso")
private int idCurso;
@Column(name = "id_persona")
private int idPersona;
@Column(name = "fecha")
private String fecha;
@Column(name = "comentario")
private String comentario;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public int getIdCurso() {
return this.idCurso;
}
public void setIdCurso(int idCurso) {
this.idCurso = idCurso;
}
public int getIdPersona() {
return this.idPersona;
}
public void setIdPersona(int idPersona) {
this.idPersona = idPersona;
}
public String getFecha() {
return this.fecha;
}
public void setFecha(String fecha) {
this.fecha = fecha;
}
public String getComentario() {
return this.comentario;
}
public void setComentario(String comentario) {
this.comentario = comentario;
}
}
