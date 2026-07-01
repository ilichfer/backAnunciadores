package  com.anunciadores.service.interfaces;
import com.anunciadores.dto.AsignacionConsolidacionDto;
import com.anunciadores.dto.PersonaConsolidacionDto;
import java.util.List;
public interface IConsolidacionService {
  Boolean asignarPersonaAPadreEspiritual(int paramInt1, int paramInt2);
  List<PersonaConsolidacionDto> listarservidoresConsolidacion(String paramString);
  List<AsignacionConsolidacionDto> listarConsolidacionByServidor(int paramInt);
  void saveDescripcionConsolidacion(int paramInt, String paramString);
}
