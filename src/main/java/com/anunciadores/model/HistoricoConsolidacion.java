package  com.anunciadores.model;
import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "his_consolidacion")
public class HistoricoConsolidacion
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@Basic(optional = false)
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
@NotNull
private int id;
@Column(name = "id_consolidacion")
private int idConsolidacion;
@Column(name = "fecha_registro_consolidacion")
private Date fechaRegistroConsolidacion;
@Column(name = "descripcion_consolidacion")
private String descripcionConsolidacion;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public int getIdConsolidacion() {
return this.idConsolidacion;
}
public void setIdConsolidacion(int idConsolidacion) {
this.idConsolidacion = idConsolidacion;
}
public Date getFechaRegistroConsolidacion() {
return this.fechaRegistroConsolidacion;
}
public void setFechaRegistroConsolidacion(Date fechaRegistroConsolidacion) {
this.fechaRegistroConsolidacion = fechaRegistroConsolidacion;
}
public String getDescripcionConsolidacion() {
return this.descripcionConsolidacion;
}
public void setDescripcionConsolidacion(String descripcionConsolidacion) {
this.descripcionConsolidacion = descripcionConsolidacion;
}
}
