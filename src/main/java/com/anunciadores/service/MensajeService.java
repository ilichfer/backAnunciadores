package  com.anunciadores.service;
import com.anunciadores.dto.MensajesDTO;
import com.anunciadores.mapper.mapperMensaje;
import com.anunciadores.model.Mensajes;
import com.anunciadores.model.Persona;
import com.anunciadores.repository.IMensajesRepo;
import com.anunciadores.repository.IPersonaRepo;
import com.anunciadores.service.interfaces.IMensajeService;
import com.anunciadores.util.UtilDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class MensajeService
implements IMensajeService
{
private IMensajesRepo mensajesRepo;
@Autowired
private UtilDate utilDate;
@Autowired
private mapperMensaje mapperMensaje;
@Autowired
private IPersonaRepo personaRepo;
public MensajeService(IMensajesRepo mensajesRepo) {
this.mensajesRepo = mensajesRepo;
}
public List<MensajesDTO> buscarMensaje(Integer idPersona) {
return this.mapperMensaje.listEntityToMensajesDTO(this.mensajesRepo.findMensajesByIdPersona(idPersona.intValue()));
}
public List<MensajesDTO> buscarTodosMensaje(Integer idPersona) {
List<MensajesDTO> lista = this.mapperMensaje.listEntityToMensajesDTO(this.mensajesRepo.findMensajesByIdPersona(idPersona.intValue()));
for (MensajesDTO dto : lista) {
if (dto.getFechaRegistro() != null) {
dto.setFechaMostrar(this.utilDate.convertDateToString(dto.getFechaRegistro()));
}
} 
return lista;
}
public MensajesDTO buscarMensajeXId(Integer idMensaje) {
Mensajes mensajeSave = this.mensajesRepo.findById(idMensaje).get();
mensajeSave.setActivo(false);
return this.mapperMensaje.EntityToMensajesDTO((Mensajes)this.mensajesRepo.save(mensajeSave));
}
public MensajesDTO guardarMensaje(MensajesDTO mensaje) {
return this.mapperMensaje.EntityToMensajesDTO((Mensajes)this.mensajesRepo.save(this.mapperMensaje.MensajesDTOToEntity(mensaje)));
}
public void enviarTodosMensajes(MensajesDTO mensaje) {
List<Persona> personas = this.personaRepo.findUsuarios();
for (Persona per : personas) {
mensaje.setDestinatario(per);
this.mensajesRepo.save(this.mapperMensaje.MensajesDTOToEntity(mensaje));
} 
}
}
