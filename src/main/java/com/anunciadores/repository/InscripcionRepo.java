package  com.anunciadores.repository;
import com.anunciadores.model.Inscripciones;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InscripcionRepo extends JpaRepository<Inscripciones, Integer> {}
