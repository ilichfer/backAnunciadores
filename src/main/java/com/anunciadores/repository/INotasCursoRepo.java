package  com.anunciadores.repository;
import com.anunciadores.model.NotasCurso;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface INotasCursoRepo extends JpaRepository<NotasCurso, Integer> {
  @Query(nativeQuery = true, value = "select nc.* from curso c join notas_curso nc on c.id  = nc.id_curso where nc.id_curso = :idCurso and nc.id_persona = :idPersona ")
  NotasCurso findNotasByCurso(@Param("idCurso")int paramInt1,@Param("idPersona") int paramInt2);
  @Query(nativeQuery = true, value = "select nc.* from notas_curso nc where nc.id_persona = :idPersona ")
  Optional<List<NotasCurso>> findHistoricoNotas(@Param("idPersona")int paramInt);
  @Query("SELECT nc FROM NotasCurso nc JOIN FETCH nc.curso WHERE nc.persona.id = :idPersona")
  List<NotasCurso> findHistoricoNotasConCurso(@Param("idPersona") int idPersona);
}
