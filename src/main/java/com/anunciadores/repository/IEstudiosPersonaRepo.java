package  com.anunciadores.repository;
import com.anunciadores.model.EstudioPersona;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IEstudiosPersonaRepo extends JpaRepository<EstudioPersona, Integer> {
  List<EstudioPersona> findByIdPersona(int paramInt);
}
