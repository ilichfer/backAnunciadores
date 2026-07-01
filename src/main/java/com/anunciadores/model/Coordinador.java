package  com.anunciadores.model;
import com.anunciadores.model.Persona;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Entity
@Table(name = "coordinador")
public class Coordinador
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private int id;
@Column(name = "fecha_servicio")
public Date fechaServicio;
@Column(name = "notas_servicio")
public String notasServicio;
@OneToOne(cascade = {CascadeType.ALL})
@JoinColumn(name = "id_persona", referencedColumnName = "id")
private Persona persona;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public Date getFechaServicio() {
return this.fechaServicio;
}
public void setFechaServicio(Date fechaServicio) {
this.fechaServicio = fechaServicio;
}
public Persona getPersona() {
return this.persona;
}
public void setPersona(Persona persona) {
this.persona = persona;
}
public String getNotasServicio() {
return this.notasServicio;
}
public void setNotasServicio(String notasServicio) {
this.notasServicio = notasServicio;
}
}
