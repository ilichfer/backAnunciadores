package  com.anunciadores.mapper;
import com.anunciadores.dto.MensajesDTO;
import com.anunciadores.model.Mensajes;
import com.anunciadores.util.UtilDate;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
@Mapper(componentModel = "spring")
public interface mapperMensaje
{
@Autowired
public static final UtilDate utilDate = new UtilDate();
public static final com.anunciadores.mapper.mapperMensaje INSTANCE = (com.anunciadores.mapper.mapperMensaje)Mappers.getMapper(com.anunciadores.mapper.mapperMensaje.class);
MensajesDTO EntityToMensajesDTO(Mensajes paramMensajes);
Mensajes MensajesDTOToEntity(MensajesDTO paramMensajesDTO);
List<MensajesDTO> listEntityToMensajesDTO(List<Mensajes> paramList);
}
