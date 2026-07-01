package  com.anunciadores.dto;
import java.io.Serializable;
public class RolPersonaDto
implements Serializable
{
private static final long serialVersionUID = 1L;
private int id;
private int idPersona;
private int idRol;
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
public int getIdRol() {
return this.idRol;
}
public void setIdRol(int idRol) {
this.idRol = idRol;
}
public RolPersonaDto(int id, int idPersona, int idRol) {
this.id = id;
this.idPersona = idPersona;
this.idRol = idRol;
}
public RolPersonaDto() {}
}
