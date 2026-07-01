package  com.anunciadores.dto;
import com.anunciadores.dto.PersonaMinisterio;
import java.util.List;
public class RequestAgregarMinisterio
{
public List<PersonaMinisterio> getListaSeleccionados() {
return this.listaSeleccionados;
}
private List<PersonaMinisterio> listaSeleccionados;
public void setListaSeleccionados(List<PersonaMinisterio> listaSeleccionados) {
this.listaSeleccionados = listaSeleccionados;
}
}
