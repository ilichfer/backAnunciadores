package  com.anunciadores.service;
import com.anunciadores.dto.ItemCombo;
import com.anunciadores.dto.ListasCombos;
import com.anunciadores.enums.ECombos;
import com.anunciadores.mapper.mapperParametros;
import com.anunciadores.repository.IParametrosRepo;
import com.anunciadores.service.interfaces.ICombos;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class CombosServiceImpl
implements ICombos
{
@Autowired
private IParametrosRepo parametrosRepo;
@Autowired
private mapperParametros mapperParametros;
private final Logger log = LoggerFactory.getLogger(com.anunciadores.service.CombosServiceImpl.class);
public ListasCombos listarParametros() {
ListasCombos listas = new ListasCombos();
List<ItemCombo> listaGenero = this.mapperParametros.listEntitytoListDto(this.parametrosRepo.findByGrupo(ECombos.GENERO.toString()));
List<ItemCombo> listaestadoCivil = this.mapperParametros.listEntitytoListDto(this.parametrosRepo.findByGrupo(ECombos.ESTADOCIVIL.toString()));
List<ItemCombo> listaEscolaridad = this.mapperParametros.listEntitytoListDto(this.parametrosRepo.findByGrupo(ECombos.ESCOLARIDAD.toString()));
List<ItemCombo> listaDocumentos = this.mapperParametros.listEntitytoListDto(this.parametrosRepo.findByGrupo(ECombos.DOCUMENTO.toString()));
listas.setListaGenero(listaGenero);
listas.setEstadoCivil(listaestadoCivil);
listas.setListaEscolaridad(listaEscolaridad);
listas.setListaDocuemntos(listaDocumentos);
return listas;
}
}
