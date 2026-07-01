package  com.anunciadores.dto;
import com.anunciadores.dto.MinistryMember;
import java.util.List;
public class MinistryDto
{
private Integer id;
private String name;
private List<MinistryMember> positions;
public MinistryDto() {}
public MinistryDto(Integer id, String name, List<MinistryMember> positions) {
this.id = id;
this.name = name;
this.positions = positions;
}
public Integer getId() {
return this.id;
}
public void setId(Integer id) {
this.id = id;
}
public String getName() {
return this.name;
}
public void setName(String name) {
this.name = name;
}
public List<MinistryMember> getPositions() {
return this.positions;
}
public void setPositions(List<MinistryMember> positions) {
this.positions = positions;
}
public String toString() {
return "Ministry{id='" + this.id + "', name='" + this.name + "', positions=" + this.positions + "}";
}
}
