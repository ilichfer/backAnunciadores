package  com.anunciadores.repository;
import com.anunciadores.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IPagoRepo extends JpaRepository<Pago, Integer> {}
