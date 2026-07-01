package  com.anunciadores.service;
import com.anunciadores.repository.IRolesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("userDetailService")
public class UsuarioService {
  @Autowired
  private IRolesRepo rolesDao;
}
