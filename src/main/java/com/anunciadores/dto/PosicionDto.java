package  com.anunciadores.dto;
import com.anunciadores.dto.PersonaDto;
import java.io.Serializable;
public class PosicionDto
implements Serializable
{
private static final long serialVersionUID = 1L;
private int id;
String nombrePosicion;
PersonaDto personaDto;
String asistencia;
private int idMinisterio;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public String getNombrePosicion() {
return this.nombrePosicion;
}
public void setNombrePosicion(String nombrePosicion) {
this.nombrePosicion = nombrePosicion;
}
public PersonaDto getPersonaDto() {
return this.personaDto;
}
public void setPersonaDto(PersonaDto personaDto) {
this.personaDto = personaDto;
}
public int getIdMinisterio() {
return this.idMinisterio;
}
public void setIdMinisterio(int idMinisterio) {
this.idMinisterio = idMinisterio;
}
public String getAsistencia() {
return this.asistencia;
}
public void setAsistencia(String asistencia) {
this.asistencia = asistencia;
}
}
