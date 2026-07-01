package  com.anunciadores.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "posiciones_ministerios")
public class PosicionesMinisterio
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private int id;
@Column(name = "id_ministerio")
private int idMinisterio;
@Column(name = "nombre_posicion")
private String nombrePosicion;
public String toString() {
return "PosicionesMinisterio{id=" + this.id + ", idMinisterio=" + this.idMinisterio + ", nombrePosicion='" + this.nombrePosicion + "'}";
}
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
public int getIdMinisterio() {
return this.idMinisterio;
}
public void setIdMinisterio(int idMinisterio) {
this.idMinisterio = idMinisterio;
}
}
