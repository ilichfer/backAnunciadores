package  com.anunciadores.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "pago")
public class Pago
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private int id;
@Column(name = "idpersona")
private int idpersona;
@Column(name = "fecha_Pago")
private String fechaPago;
@Column(name = "valor")
private int valor;
@Column(name = "id_curso")
private int id_curso;
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
public String getFechaPago() {
return this.fechaPago;
}
public void setFechaPago(String fechaPago) {
this.fechaPago = fechaPago;
}
public int getValor() {
return this.valor;
}
public void setValor(int valor) {
this.valor = valor;
}
public int getId_curso() {
return this.id_curso;
}
public void setId_curso(int id_curso) {
this.id_curso = id_curso;
}
public Pago(int id, int idpersona, String fechaPago, int valor, int id_curso) {
this.id = id;
this.idpersona = idpersona;
this.fechaPago = fechaPago;
this.valor = valor;
this.id_curso = id_curso;
}
public Pago() {}
}
