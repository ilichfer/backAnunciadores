package  com.anunciadores.dto;
import com.anunciadores.dto.Ministry;
import java.util.List;
public class MinistriesResponse
{
private List<Ministry> ministries;
public MinistriesResponse(List<Ministry> ministries) {
this.ministries = ministries;
}
public MinistriesResponse() {}
public List<Ministry> getMinistries() {
return this.ministries;
}
public void setMinistries(List<Ministry> ministries) {
this.ministries = ministries;
}
}
