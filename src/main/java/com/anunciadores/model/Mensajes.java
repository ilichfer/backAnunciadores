package  com.anunciadores.model;
import com.anunciadores.model.Persona;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Entity
@Table(name = "mensajes")
public class Mensajes
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private int id;
@Column(name = "descripcion")
private String descripcion;
@OneToOne
@JoinColumn(name = "remitente", referencedColumnName = "id")
private Persona remitente;
@OneToOne
@JoinColumn(name = "destinatario", referencedColumnName = "id")
private Persona destinatario;
@Column(name = "fecha_registro")
Date fechaRegistro;
@Column(name = "activo")
private boolean activo;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public String getDescripcion() {
return this.descripcion;
}
public void setDescripcion(String descripcion) {
this.descripcion = descripcion;
}
public Persona getRemitente() {
return this.remitente;
}
public void setRemitente(Persona remitente) {
this.remitente = remitente;
}
public Persona getDestinatario() {
return this.destinatario;
}
public void setDestinatario(Persona destinatario) {
this.destinatario = destinatario;
}
public Date getFechaRegistro() {
return this.fechaRegistro;
}
public void setFechaRegistro(Date fechaRegistro) {
this.fechaRegistro = fechaRegistro;
}
public boolean isActivo() {
return this.activo;
}
public void setActivo(boolean activo) {
this.activo = activo;
}
}
