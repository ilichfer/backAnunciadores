package  com.anunciadores.dto;
import java.io.Serializable;
import java.util.List;
public class PermisosDto
implements Serializable
{
private static final long serialVersionUID = 1L;
private List<Integer> id;
public List<String> nombreBotonMenu;
public List<Boolean> estado;
public List<Integer> getId() {
return this.id;
}
public void setId(List<Integer> id) {
this.id = id;
}
public List<String> getNombreBotonMenu() {
return this.nombreBotonMenu;
}
public void setNombreBotonMenu(List<String> nombreBotonMenu) {
this.nombreBotonMenu = nombreBotonMenu;
}
public List<Boolean> getEstado() {
return this.estado;
}
public void setEstado(List<Boolean> estado) {
this.estado = estado;
}
}
