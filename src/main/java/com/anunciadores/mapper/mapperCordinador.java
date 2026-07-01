package  com.anunciadores.mapper;
import com.anunciadores.dto.CoordinadorDTO;
import com.anunciadores.model.Coordinador;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface mapperCordinador
{
public static final com.anunciadores.mapper.mapperCordinador INSTANCE = (com.anunciadores.mapper.mapperCordinador)Mappers.getMapper(com.anunciadores.mapper.mapperCordinador.class);
CoordinadorDTO EntitytoCoordinadorDTO(Coordinador paramCoordinador);
List<CoordinadorDTO> listEntitytoCoordinadorDTO(List<Coordinador> paramList);
}
