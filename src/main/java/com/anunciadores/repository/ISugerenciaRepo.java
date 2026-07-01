package  com.anunciadores.repository;
import com.anunciadores.model.Sugerencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ISugerenciaRepo extends JpaRepository<Sugerencia, Integer> {}
