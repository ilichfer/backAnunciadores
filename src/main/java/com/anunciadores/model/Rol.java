package  com.anunciadores.model;
import com.sun.istack.NotNull;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "rol")
public class Rol
implements Serializable
{
public static final int ID_ADMIN = 1;
public static final int ID_USER = 2;
public static final int ID_USUARIO = 3;
private static final long serialVersionUID = 1L;
@Id
@Basic(optional = false)
@Column(name = "id")
@NotNull
private int id;
@Column(name = "descripcion_rol")
private String descripcion;
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
public Rol(int id, String descripcion) {
this.id = id;
this.descripcion = descripcion;
}
public Rol() {}
}
