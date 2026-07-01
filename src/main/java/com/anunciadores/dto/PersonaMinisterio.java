package  com.anunciadores.dto;
import java.io.Serializable;
import java.util.List;
public class PersonaMinisterio
implements Serializable
{
private static final long serialVersionUID = 1L;
private int id;
private List<Integer> idPersona;
private int idMinisterio;
private List<String> selecionado;
private boolean aceptarTerminos;
private List<Long> categoriasSeleccionadas;
public List<Long> getCategoriasSeleccionadas() {
return this.categoriasSeleccionadas;
}
public void setCategoriasSeleccionadas(List<Long> categoriasSeleccionadas) {
this.categoriasSeleccionadas = categoriasSeleccionadas;
}
public boolean isAceptarTerminos() {
return this.aceptarTerminos;
}
public void setAceptarTerminos(boolean aceptarTerminos) {
this.aceptarTerminos = aceptarTerminos;
}
public List<Integer> getIdPersona() {
return this.idPersona;
}
public void setIdPersona(List<Integer> idPersona) {
this.idPersona = idPersona;
}
public int getIdMinisterio() {
return this.idMinisterio;
}
public void setIdMinisterio(int idMinisterio) {
this.idMinisterio = idMinisterio;
}
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public List<String> getSelecionado() {
return this.selecionado;
}
public void setSelecionado(List<String> selecionado) {
this.selecionado = selecionado;
}
}
