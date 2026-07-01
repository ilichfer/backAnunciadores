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
@Table(name = "inscripcion_actividades")
public class InscripcionActividad
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
@Column(name = "id_actividad")
private int idActividad;
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
public int getIdActividad() {
return this.idActividad;
}
public void setIdActividad(int idActividad) {
this.idActividad = idActividad;
}
}
