package  com.anunciadores.dto;
import com.anunciadores.dto.PositionDto;
import java.util.List;
public class Ministry
{
private String id;
private String name;
private List<PositionDto> positions;
public Ministry() {}
public Ministry(String id, String name, List<PositionDto> positions) {
this.id = id;
this.name = name;
this.positions = positions;
}
public String getId() {
return this.id;
}
public void setId(String id) {
this.id = id;
}
public String getName() {
return this.name;
}
public void setName(String name) {
this.name = name;
}
public List<PositionDto> getPositions() {
return this.positions;
}
public void setPositions(List<PositionDto> positions) {
this.positions = positions;
}
public String toString() {
return "Ministry{id='" + this.id + "', name='" + this.name + "', positions=" + this.positions + "}";
}
}
