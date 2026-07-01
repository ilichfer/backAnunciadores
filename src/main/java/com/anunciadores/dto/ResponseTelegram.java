package  com.anunciadores.dto;
import com.anunciadores.dto.ResultDTO;
import java.util.ArrayList;
public class ResponseTelegram {
public boolean ok;
public boolean isOk() {
return this.ok;
}
public ArrayList<ResultDTO> result;
public void setOk(boolean ok) {
this.ok = ok;
}
public ArrayList<ResultDTO> getResult() {
return this.result;
}
public void setResult(ArrayList<ResultDTO> result) {
this.result = result;
}
}
