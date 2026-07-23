package  com.anunciadores.repository;
import com.anunciadores.model.Inscripciones;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
public interface InscripcionRepo extends JpaRepository<Inscripciones, Integer> {
  Optional<Inscripciones> findByIdCursoAndIdPersona(int idCurso, int idPersona);
  List<Inscripciones> findByIdCurso(int idCurso);
  @Modifying
  @Transactional
  @Query("DELETE FROM Inscripciones i WHERE i.idCurso = ?1 AND i.idPersona = ?2")
  void deleteByIdCursoAndIdPersona(int idCurso, int idPersona);
}
