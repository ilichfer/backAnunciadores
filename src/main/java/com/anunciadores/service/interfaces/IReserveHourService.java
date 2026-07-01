package  com.anunciadores.service.interfaces;
import com.anunciadores.model.TimeSlot;
import java.util.List;
public interface IReserveHourService {
  List<TimeSlot> reserveSlot();
}
