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
@Table(name = "notas")
public class Notas
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private int id;
@Column(name = "idcurso")
private Date idcurso;
@Column(name = "nota")
private int nota;
@Column(name = "nomactividad")
private String actividad;
@Column(name = "idpersonafk")
private int idpersona;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public int getIdpersona() {
return this.idpersona;
}
public void setIdpersona(int idpersona) {
this.idpersona = idpersona;
}
public Date getIdcurso() {
return this.idcurso;
}
public void setIdcurso(Date idcurso) {
this.idcurso = idcurso;
}
public int getNota() {
return this.nota;
}
public void setNota(int nota) {
this.nota = nota;
}
public String getActividad() {
return this.actividad;
}
public void setActividad(String actividad) {
this.actividad = actividad;
}
}
