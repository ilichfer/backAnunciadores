package  com.anunciadores.dto;
import com.anunciadores.model.Persona;
import java.io.Serializable;
import java.util.Date;
public class TdcDto
implements Serializable
{
private static final long serialVersionUID = 1L;
private int id;
String tdc;
Persona persona;
String fechaCreacion;
String nombredocumento;
String urlImage;
public String getUrlImage() {
return this.urlImage;
}
public void setUrlImage(String urlImage) {
this.urlImage = urlImage;
}
public String getTdc() {
return this.tdc;
}
public void setTdc(String tdc) {
this.tdc = tdc;
}
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public Persona getPersona() {
return this.persona;
}
public void setPersona(Persona persona) {
this.persona = persona;
}
    public String getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
public String getNombredocumento() {
return this.nombredocumento;
}
public void setNombredocumento(String nombredocumento) {
this.nombredocumento = nombredocumento;
}
}
