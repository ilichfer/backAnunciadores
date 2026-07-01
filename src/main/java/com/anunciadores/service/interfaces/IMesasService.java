package  com.anunciadores.service.interfaces;
import com.anunciadores.model.Actividad;
import com.anunciadores.model.Mesa;
import com.anunciadores.model.Persona;
import java.util.List;
public interface IMesasService {
  List<Actividad> listarActiviades();
  List<Persona> BuscarPersonasSinMesa(Mesa paramMesa);
  void delete(Mesa paramMesa);
}
