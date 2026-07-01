package  com.anunciadores.repository;
import com.anunciadores.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ITimeSlotRepo extends JpaRepository<TimeSlot, Integer> {}
