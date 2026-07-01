package  com.anunciadores.repository;
import com.anunciadores.model.ParametrosCombos;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
public interface IParametrosRepo extends JpaRepository<ParametrosCombos, Integer> {
  List<ParametrosCombos> findByGrupo(@Param("grupo") String paramString);
}
