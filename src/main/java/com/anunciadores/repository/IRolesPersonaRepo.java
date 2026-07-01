package  com.anunciadores.repository;
import com.anunciadores.model.RolPersona;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface IRolesPersonaRepo extends JpaRepository<RolPersona, Integer> {
  @Modifying
  @Query("select u from RolPersona u  where u.idPersona = :idPersona")
  List<RolPersona> findRolByidPersona(@Param("idPersona") int paramInt);
}
