package  com.anunciadores.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "consolidacion")
public class Consolidacion
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private int id;
@Column(name = "id_persona")
private int idPersona;
@Column(name = "acepta_consolidacion")
private Boolean aceptaConsolidacion;
@Column(name = "horario_consolidacion_sugerido")
private String horarioConsolidacionSugerido;
@Column(name = "horario_consolidacion_persona")
private String horarioConsolidacionPersona;
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
public String getHorarioConsolidacionSugerido() {
return this.horarioConsolidacionSugerido;
}
public void setHorarioConsolidacionSugerido(String horarioConsolidacionSugerido) {
this.horarioConsolidacionSugerido = horarioConsolidacionSugerido;
}
public String getHorarioConsolidacionPersona() {
return this.horarioConsolidacionPersona;
}
public void setHorarioConsolidacionPersona(String horarioConsolidacionPersona) {
this.horarioConsolidacionPersona = horarioConsolidacionPersona;
}
public Boolean getAceptaConsolidacion() {
return this.aceptaConsolidacion;
}
public void setAceptaConsolidacion(Boolean aceptaConsolidacion) {
this.aceptaConsolidacion = aceptaConsolidacion;
}
}
