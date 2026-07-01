package  com.anunciadores.mapper;
import com.anunciadores.dto.ItemCombo;
import com.anunciadores.model.ParametrosCombos;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface mapperParametros
{
public static final com.anunciadores.mapper.mapperParametros INSTANCE = (com.anunciadores.mapper.mapperParametros)Mappers.getMapper(com.anunciadores.mapper.mapperParametros.class);
List<ItemCombo> listEntitytoListDto(List<ParametrosCombos> paramList);
}
