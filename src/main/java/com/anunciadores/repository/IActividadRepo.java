package  com.anunciadores.repository;
import com.anunciadores.model.Actividad;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface IActividadRepo extends JpaRepository<Actividad, Integer> {
  @Query(" SELECT ac from Actividad ac join InscripcionActividad ia on (ac.id = ia.idActividad)  WHERE ia.idPersona = ?1")
  List<Actividad> findActividadesByPersona(int paramInt);
}
