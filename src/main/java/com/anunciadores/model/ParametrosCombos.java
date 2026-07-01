package  com.anunciadores.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "parametros")
public class ParametrosCombos
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private int id;
@Column(name = "descripcion")
private String descripcion;
@Column(name = "grupo")
private String grupo;
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
public String getGrupo() {
return this.grupo;
}
public void setGrupo(String grupo) {
this.grupo = grupo;
}
}
