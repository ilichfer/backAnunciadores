package  com.anunciadores.repository;
import com.anunciadores.model.ParamMenu;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IParamMenuRepo extends JpaRepository<ParamMenu, Integer> {
  ParamMenu findByNombreBotonMenu(String paramString);
}
