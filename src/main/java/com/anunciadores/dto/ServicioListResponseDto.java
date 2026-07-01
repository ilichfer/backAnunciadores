package  com.anunciadores.dto;
import com.anunciadores.dto.ServicioResponseDto;
import java.io.Serializable;
import java.util.List;
public class ServicioListResponseDto
implements Serializable
{
private List<ServicioResponseDto> servicioDTO;
private String fechaServcio;
private String nombreMinisterio;
private Integer idMinisterio;
private Integer tamanoLista;
public List<ServicioResponseDto> getServicioDTO() {
return this.servicioDTO;
}
public void setServicioDTO(List<ServicioResponseDto> servicioDTO) {
this.servicioDTO = servicioDTO;
}
public String getFechaServcio() {
return this.fechaServcio;
}
public void setFechaServcio(String fechaServcio) {
this.fechaServcio = fechaServcio;
}
public String getNombreMinisterio() {
return this.nombreMinisterio;
}
public void setNombreMinisterio(String nombreMinisterio) {
this.nombreMinisterio = nombreMinisterio;
}
public Integer getIdMinisterio() {
return this.idMinisterio;
}
public void setIdMinisterio(Integer idMinisterio) {
this.idMinisterio = idMinisterio;
}
public Integer getTamanoLista() {
return this.tamanoLista;
}
public void setTamanoLista(Integer tamanoLista) {
this.tamanoLista = tamanoLista;
}
}
