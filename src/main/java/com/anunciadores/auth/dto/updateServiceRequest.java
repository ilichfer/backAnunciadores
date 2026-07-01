package  com.anunciadores.auth.dto;
import com.anunciadores.auth.dto.assignments;
import java.util.List;
public class updateServiceRequest
{
private String date;
private int ministryId;
private String ministry;
private List<assignments> assignments;
public String getDate() {
return this.date;
}
public void setDate(String date) {
this.date = date;
}
public int getMinistryId() {
return this.ministryId;
}
public void setMinistryId(int ministryId) {
this.ministryId = ministryId;
}
public String getMinistry() {
return this.ministry;
}
public void setMinistry(String ministry) {
this.ministry = ministry;
}
public List<assignments> getAssignments() {
return this.assignments;
}
public void setAssignments(List<assignments> assignments) {
this.assignments = assignments;
}
}
