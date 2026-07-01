package  com.anunciadores.model;
import com.sun.istack.NotNull;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "persona_mesa")
public class MesaPersona
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@Basic(optional = false)
@Column(name = "id")
@NotNull
private int id;
@Column(name = "id_mesa")
private int idMesa;
@Column(name = "id_persona")
private String idPersona;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public int getIdMesa() {
return this.idMesa;
}
public void setIdMesa(int idMesa) {
this.idMesa = idMesa;
}
public String getIdPersona() {
return this.idPersona;
}
public void setIdPersona(String idPersona) {
this.idPersona = idPersona;
}
}
