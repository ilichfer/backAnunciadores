package  com.anunciadores.model;
import com.anunciadores.model.Persona;
import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
@Table(name = "sugerencia")
public class Sugerencia
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@Basic(optional = false)
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
@NotNull
private int id;
@OneToOne(cascade = {CascadeType.ALL})
@JoinColumn(name = "id_persona", referencedColumnName = "id")
private Persona persona;
@Column(name = "descripcion")
private String descripcion;
@Column(name = "fecha_registro")
Date fechaRegistro;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public Persona getPersona() {
return this.persona;
}
public void setPersona(Persona persona) {
this.persona = persona;
}
public String getDescripcion() {
return this.descripcion;
}
public void setDescripcion(String descripcion) {
this.descripcion = descripcion;
}
public Date getFechaRegistro() {
return this.fechaRegistro;
}
public void setFechaRegistro(Date fechaRegistro) {
this.fechaRegistro = fechaRegistro;
}
}
