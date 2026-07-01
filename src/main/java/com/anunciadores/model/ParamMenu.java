package  com.anunciadores.model;
import com.anunciadores.model.ParamSubMenu;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
@Entity
@Table(name = "param_menu")
public class ParamMenu
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private int id;
@Column(name = "nombre_boton_menu")
public String nombreBotonMenu;
@OneToMany(cascade = {CascadeType.ALL}, mappedBy = "idMenu")
private List<ParamSubMenu> subMenu;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public String getNombreBotonMenu() {
return this.nombreBotonMenu;
}
public void setNombreBotonMenu(String nombreBotonMenu) {
this.nombreBotonMenu = nombreBotonMenu;
}
public List<ParamSubMenu> getSubMenu() {
return this.subMenu;
}
public void setSubMenu(List<ParamSubMenu> subMenu) {
this.subMenu = subMenu;
}
}
