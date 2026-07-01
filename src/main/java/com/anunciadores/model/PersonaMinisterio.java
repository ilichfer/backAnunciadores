package  com.anunciadores.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "persona_ministerio")
public class PersonaMinisterio
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private int id;
@Column(name = "id_persona")
private int idPersona;
@Column(name = "id_ministerio")
private int idMinisterio;
public int getIdPersona() {
return this.idPersona;
}
public void setIdPersona(int idPersona) {
this.idPersona = idPersona;
}
public int getIdMinisterio() {
return this.idMinisterio;
}
public void setIdMinisterio(int idMinisterio) {
this.idMinisterio = idMinisterio;
}
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
}
