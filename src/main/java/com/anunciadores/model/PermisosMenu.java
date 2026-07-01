package  com.anunciadores.model;
import com.anunciadores.model.ParamMenu;
import java.io.Serializable;
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
@Table(name = "permisos_menu")
public class PermisosMenu
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private int id;
@Column(name = "id_persona")
private int idPersona;
@Column(name = "nombre_boton_menu")
public String nombreBotonMenu;
@Column(name = "estado")
public String estado;
@OneToOne(cascade = {CascadeType.ALL})
@JoinColumn(name = "id_menu", referencedColumnName = "id")
private ParamMenu menu;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public int getIdPersona() {
return this.idPersona;
}
public void setIdPersona(int idPersona) {
this.idPersona = idPersona;
}
public String getNombreBotonMenu() {
return this.nombreBotonMenu;
}
public void setNombreBotonMenu(String nombreBotonMenu) {
this.nombreBotonMenu = nombreBotonMenu;
}
public String getEstado() {
return this.estado;
}
public void setEstado(String estado) {
this.estado = estado;
}
public ParamMenu getMenu() {
return this.menu;
}
public void setMenu(ParamMenu menu) {
this.menu = menu;
}
}
