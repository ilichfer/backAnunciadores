package  com.anunciadores.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "param_submenu")
public class ParamSubMenu
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private int id;
@Column(name = "idMenu")
private int idMenu;
@Column(name = "etiqueta")
public String etiqueta;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public int getIdMenu() {
return this.idMenu;
}
public void setIdMenu(int idMenu) {
this.idMenu = idMenu;
}
public String getEtiqueta() {
return this.etiqueta;
}
public void setEtiqueta(String etiqueta) {
this.etiqueta = etiqueta;
}
}
