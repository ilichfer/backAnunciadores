package  com.anunciadores.repository;
import com.anunciadores.model.Consolidacion;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IConsolidacionRepo extends JpaRepository<Consolidacion, Integer> {
  Consolidacion findByIdPersona(int paramInt);
}
