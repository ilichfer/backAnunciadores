package  com.anunciadores.service.interfaces;
import com.anunciadores.dto.MensajesDTO;
import java.util.List;
public interface IMensajeService {
  List<MensajesDTO> buscarMensaje(Integer paramInteger);
  List<MensajesDTO> buscarTodosMensaje(Integer paramInteger);
  MensajesDTO buscarMensajeXId(Integer paramInteger);
  MensajesDTO guardarMensaje(MensajesDTO paramMensajesDTO);
  void enviarTodosMensajes(MensajesDTO paramMensajesDTO);
}
