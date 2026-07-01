package  com.anunciadores.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "inscripcion_consolidacion")
public class inscripcionConsolidacion
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private int id;
@Column(name = "id_padre_espiritual")
private int idPadreEspiritual;
@Column(name = "id_persona_consolidar")
private int idPersonaConsolidar;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public int getIdPadreEspiritual() {
return this.idPadreEspiritual;
}
public void setIdPadreEspiritual(int idPadreEspiritual) {
this.idPadreEspiritual = idPadreEspiritual;
}
public int getIdPersonaConsolidar() {
return this.idPersonaConsolidar;
}
public void setIdPersonaConsolidar(int idPersonaConsolidar) {
this.idPersonaConsolidar = idPersonaConsolidar;
}
public inscripcionConsolidacion(int id, int idPadreEspiritual, int idPersonaConsolidar) {
this.id = id;
this.idPadreEspiritual = idPadreEspiritual;
this.idPersonaConsolidar = idPersonaConsolidar;
}
public inscripcionConsolidacion() {}
}
