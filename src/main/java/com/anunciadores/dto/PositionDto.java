package  com.anunciadores.dto;
import java.io.Serializable;
public class PositionDto
implements Serializable
{
private static final long serialVersionUID = 1L;
private int id;
private String name;
public PositionDto() {}
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public PositionDto(int id, String name) {
this.id = id;
this.name = name;
}
public String getName() {
return this.name;
}
public void setName(String name) {
this.name = name;
}
}
