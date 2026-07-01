package  com.anunciadores.dto;
import java.time.LocalDate;
public class CordinatorDto
{
  private String name;
  private LocalDate date;
  private int id;
  private String notasServicio;
  public CordinatorDto() {}
  public CordinatorDto(String name, LocalDate date, int id) {
    this.name = name;
    this.date = date;
    this.id = id;
  }
  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public LocalDate getDate() {
    return this.date;
  }
  public void setDate(LocalDate date) {
    this.date = date;
  }
  public int getId() {
    return this.id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getNotasServicio() {
    return this.notasServicio;
  }
  public void setNotasServicio(String notasServicio) {
    this.notasServicio = notasServicio;
  }
}
