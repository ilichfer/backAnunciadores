package  com.anunciadores.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "ministerios")
public class Ministerio
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private int id;
@Column(name = "nombre")
String nombre;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public String getNombre() {
return this.nombre;
}
public void setNombre(String nombre) {
this.nombre = nombre;
}
}
