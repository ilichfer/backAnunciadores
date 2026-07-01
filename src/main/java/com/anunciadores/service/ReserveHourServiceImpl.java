package  com.anunciadores.service;
import com.anunciadores.model.TimeSlot;
import com.anunciadores.service.interfaces.IReserveHourService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class ReserveHourServiceImpl
implements IReserveHourService
{
private Logger LOGGER = LoggerFactory.getLogger(com.anunciadores.service.ReserveHourServiceImpl.class);
public List<TimeSlot> reserveSlot() {
List<TimeSlot> slots = new ArrayList<>();
LocalDate today = LocalDate.now();
LocalTime start = LocalTime.of(7, 0);
int totalBlocks = 48;
for (int i = 0; i < totalBlocks; i++) {
LocalTime blockStart = start.plusMinutes((i * 30));
LocalDate blockDate = today;
TimeSlot slot = new TimeSlot();
slot.setDiaSiguiente(false);
if (blockStart.isBefore(start)) {
blockDate = today.plusDays(1L);
slot.setDiaSiguiente(true);
} 
slot.setDate(blockDate);
slot.setStartTime(blockStart);
slot.setReserved(false);
slots.add(slot);
} 
return slots;
}
}
