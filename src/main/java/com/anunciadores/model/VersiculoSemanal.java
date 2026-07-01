package  com.anunciadores.model;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "versiculo_semanal")
public class VersiculoSemanal
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private int id;
@Column(name = "fecha_inicio")
public Date fechaInicio;
@Column(name = "fecha_fin")
public Date fechaFin;
@Column(name = "versiculo_texto")
public String versiculoTexto;
@Column(name = "cita_biblica")
public String citaBiblica;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public Date getFechaInicio() {
return this.fechaInicio;
}
public void setFechaInicio(Date fechaInicio) {
this.fechaInicio = fechaInicio;
}
public Date getFechaFin() {
return this.fechaFin;
}
public void setFechaFin(Date fechaFin) {
this.fechaFin = fechaFin;
}
public String getVersiculoTexto() {
return this.versiculoTexto;
}
public void setVersiculoTexto(String versiculoTexto) {
this.versiculoTexto = versiculoTexto;
}
public String getCitaBiblica() {
return this.citaBiblica;
}
public void setCitaBiblica(String citaBiblica) {
this.citaBiblica = citaBiblica;
}
}
