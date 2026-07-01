package  com.anunciadores.mapper;
import com.anunciadores.dto.NotasCursoDTO;
import com.anunciadores.model.NotasCurso;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface mapperNotas
{
public static final com.anunciadores.mapper.mapperNotas INSTANCE = (com.anunciadores.mapper.mapperNotas)Mappers.getMapper(com.anunciadores.mapper.mapperNotas.class);
NotasCursoDTO EntityToNotasCursoDTO(NotasCurso paramNotasCurso);
}
