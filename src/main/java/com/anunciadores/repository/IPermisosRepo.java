package  com.anunciadores.repository;
import com.anunciadores.model.PermisosMenu;
import com.anunciadores.model.Persona;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface IPermisosRepo extends JpaRepository<PermisosMenu, Integer> {
  @Query
  List<PermisosMenu> findByIdPersona(int paramInt);
  Optional<PermisosMenu> findByIdPersonaAndNombreBotonMenu(int paramInt, String paramString);
  @Query(value = "select p.* from permisos_menu pm join persona p on pm.id_persona  = p.id  where p.id = :idPersona and id_menu  = 9", nativeQuery = true)
  Persona findByIdPersonaAndIdMenu(@Param("idPersona")int paramInt);
}
