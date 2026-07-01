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
@Table(name = "estudio_persona")
public class EstudioPersona
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@Basic(optional = false)
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
@NotNull
private int id;
@Column(name = "id_persona")
private int idPersona;
@Column(name = "fecha_realizacion")
private String fechaRealizacion;
@Column(name = "nombre_estudio")
private String nombreEstudio;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public int getIdPersona() {
return this.idPersona;
}
public void setIdPersona(int idPersona) {
this.idPersona = idPersona;
}
public String getFechaRealizacion() {
return this.fechaRealizacion;
}
public void setFechaRealizacion(String fechaRealizacion) {
this.fechaRealizacion = fechaRealizacion;
}
public String getNombreEstudio() {
return this.nombreEstudio;
}
public void setNombreEstudio(String nombreEstudio) {
this.nombreEstudio = nombreEstudio;
}
}
