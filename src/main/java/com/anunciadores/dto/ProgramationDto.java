package  com.anunciadores.dto;
import com.anunciadores.dto.CordinatorDto;
import com.anunciadores.dto.MinistryDto;
import java.time.LocalDate;
import java.util.List;
public class ProgramationDto
{
private String id;
private LocalDate date;
private String time;
private CordinatorDto coordinator;
private List<MinistryDto> ministries;
public ProgramationDto() {}
public ProgramationDto(String id, LocalDate date, String time, CordinatorDto coordinator, List<MinistryDto> ministries) {
this.id = id;
this.date = date;
this.time = time;
this.coordinator = coordinator;
this.ministries = ministries;
}
public CordinatorDto getCoordinator() {
return this.coordinator;
}
public void setCoordinator(CordinatorDto coordinator) {
this.coordinator = coordinator;
}
public String getId() {
return this.id;
}
public void setId(String id) {
this.id = id;
}
public LocalDate getDate() {
return this.date;
}
public void setDate(LocalDate date) {
this.date = date;
}
public String getTime() {
return this.time;
}
public void setTime(String time) {
this.time = time;
}
public List<MinistryDto> getMinistries() {
return this.ministries;
}
public void setMinistries(List<MinistryDto> ministries) {
this.ministries = ministries;
}
}
