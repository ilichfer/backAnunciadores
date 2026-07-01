package  com.anunciadores.dto;
import com.anunciadores.dto.MessageDTO;
public class ResultDTO
{
public int update_id;
public MessageDTO message;
public int getUpdate_id() {
return this.update_id;
}
public void setUpdate_id(int update_id) {
this.update_id = update_id;
}
public MessageDTO getMessage() {
return this.message;
}
public void setMessage(MessageDTO message) {
this.message = message;
}
}
