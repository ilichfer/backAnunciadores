package  com.anunciadores.service.interfaces;
import com.anunciadores.model.PermisosMenu;
import java.util.List;
public interface IMenuService {
  List<PermisosMenu> findAllPermisosMenu(int paramInt);
}
