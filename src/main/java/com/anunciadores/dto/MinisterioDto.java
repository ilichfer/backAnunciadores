package  com.anunciadores.dto;
import com.anunciadores.dto.PosicionDto;
import java.io.Serializable;
public class MinisterioDto
implements Serializable
{
private static final long serialVersionUID = 1L;
private int id;
String nombreMinisterio;
PosicionDto posicionDto;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public String getNombreMinisterio() {
return this.nombreMinisterio;
}
public void setNombreMinisterio(String nombreMinisterio) {
this.nombreMinisterio = nombreMinisterio;
}
public PosicionDto getPosicionDto() {
return this.posicionDto;
}
public void setPosicionDto(PosicionDto posicionDto) {
this.posicionDto = posicionDto;
}
}
