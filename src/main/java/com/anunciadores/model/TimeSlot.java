package  com.anunciadores.model;
import com.anunciadores.model.Persona;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "tdc")
public class TimeSlot
implements Serializable
{
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private LocalDate date;
private LocalTime startTime;
private boolean reserved;
private boolean diaSiguiente;
@ManyToOne
private Persona reservedBy;
public Long getId() {
return this.id;
}
public void setId(Long id) {
this.id = id;
}
public LocalDate getDate() {
return this.date;
}
public void setDate(LocalDate date) {
this.date = date;
}
public LocalTime getStartTime() {
return this.startTime;
}
public void setStartTime(LocalTime startTime) {
this.startTime = startTime;
}
public boolean isReserved() {
return this.reserved;
}
public void setReserved(boolean reserved) {
this.reserved = reserved;
}
public Persona getReservedBy() {
return this.reservedBy;
}
public void setReservedBy(Persona reservedBy) {
this.reservedBy = reservedBy;
}
public boolean isDiaSiguiente() {
return this.diaSiguiente;
}
public void setDiaSiguiente(boolean diaSiguiente) {
this.diaSiguiente = diaSiguiente;
}
}
