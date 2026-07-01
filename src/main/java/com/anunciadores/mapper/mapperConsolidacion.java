package  com.anunciadores.mapper;
import com.anunciadores.dto.PersonaConsolidacionDto;
import com.anunciadores.model.Persona;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface mapperConsolidacion
{
public static final com.anunciadores.mapper.mapperConsolidacion INSTANCE = (com.anunciadores.mapper.mapperConsolidacion)Mappers.getMapper(com.anunciadores.mapper.mapperConsolidacion.class);
PersonaConsolidacionDto EntitytoConsolidacionDto(Persona paramPersona);
List<PersonaConsolidacionDto> liitEntitytoConsolidacionDto(List<Persona> paramList);
}
