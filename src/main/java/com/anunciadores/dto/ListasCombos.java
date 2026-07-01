package  com.anunciadores.dto;
import com.anunciadores.dto.ItemCombo;
import java.util.List;
public class ListasCombos {
private List<ItemCombo> listaGenero;
private List<ItemCombo> estadoCivil;
private List<ItemCombo> listaEscolaridad;
private List<ItemCombo> listaDocuemntos;
public List<ItemCombo> getListaGenero() {
return this.listaGenero;
}
public void setListaGenero(List<ItemCombo> listaGenero) {
this.listaGenero = listaGenero;
}
public List<ItemCombo> getEstadoCivil() {
return this.estadoCivil;
}
public void setEstadoCivil(List<ItemCombo> estadoCivil) {
this.estadoCivil = estadoCivil;
}
public List<ItemCombo> getListaEscolaridad() {
return this.listaEscolaridad;
}
public void setListaEscolaridad(List<ItemCombo> listaEscolaridad) {
this.listaEscolaridad = listaEscolaridad;
}
public List<ItemCombo> getListaDocuemntos() {
return this.listaDocuemntos;
}
public void setListaDocuemntos(List<ItemCombo> listaDocuemntos) {
this.listaDocuemntos = listaDocuemntos;
}
}
