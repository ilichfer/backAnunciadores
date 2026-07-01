package  com.anunciadores.mapper;
import com.anunciadores.dto.PersonaDto;
import com.anunciadores.model.Persona;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface mapperPersona
{
public static final com.anunciadores.mapper.mapperPersona INSTANCE = (com.anunciadores.mapper.mapperPersona)Mappers.getMapper(com.anunciadores.mapper.mapperPersona.class);
PersonaDto EntityToPersonaDto(Persona paramPersona);
List<PersonaDto> listEntityToConsolidacionDto(List<Persona> paramList);
}
