package  com.anunciadores.dto;
import java.io.Serializable;
public class AsignacionServicioDTO
implements Serializable
{
private static final long serialVersionUID = 1L;
private String MInisterio;
private String posiciones;
public String getMInisterio() {
return this.MInisterio;
}
public void setMInisterio(String MInisterio) {
this.MInisterio = MInisterio;
}
public String getPosiciones() {
return this.posiciones;
}
public void setPosiciones(String posiciones) {
this.posiciones = posiciones;
}
}
