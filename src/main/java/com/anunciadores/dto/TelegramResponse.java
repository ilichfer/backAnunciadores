package  com.anunciadores.dto;
import org.hibernate.sql.Update;
import java.util.List;
public class TelegramResponse {
  private boolean ok;
  private List<Update> result;
}
