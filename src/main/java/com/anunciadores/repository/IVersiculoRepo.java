package  com.anunciadores.repository;
import com.anunciadores.model.VersiculoSemanal;
import java.util.Date;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IVersiculoRepo extends JpaRepository<VersiculoSemanal, Integer> {
  Optional<VersiculoSemanal> findByFechaFin(Date paramDate);
  Optional<VersiculoSemanal> findByFechaFinBetween(Date paramDate1, Date paramDate2);
}
