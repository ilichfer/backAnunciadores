package  com.anunciadores.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "actividades")
public class Actividad
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private int id;
@Column(name = "nombre")
private String nombreActividad;
@Column(name = "fecha")
private String fecha;
@Column(name = "cantidad_mesas")
private int cantidadMesas;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public String getNombreActividad() {
return this.nombreActividad;
}
public void setNombreActividad(String nombreActividad) {
this.nombreActividad = nombreActividad;
}
public String getFecha() {
return this.fecha;
}
public void setFecha(String fecha) {
this.fecha = fecha;
}
public int getCantidadMesas() {
return this.cantidadMesas;
}
public void setCantidadMesas(int cantidadMesas) {
this.cantidadMesas = cantidadMesas;
}
public static long getSerialversionuid() {
return 1L;
}
}
