package  com.anunciadores.service;
import com.anunciadores.model.Actividad;
import com.anunciadores.model.Curso;
import com.anunciadores.model.Mesa;
import com.anunciadores.model.Persona;
import com.anunciadores.repository.IActividadRepo;
import com.anunciadores.repository.IMesasRepo;
import com.anunciadores.repository.IPersonaRepo;
import com.anunciadores.repository.MesasRepoImpl;
import com.anunciadores.service.interfaces.IMesasService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class MesasServiceImpl
implements IMesasService
{
@Autowired
private IActividadRepo ActividadRepository;
@Autowired
private IMesasRepo mesasRepository;
@Autowired
private MesasRepoImpl mesaDAO;
@Autowired
private IPersonaRepo personaRepo;
List<Curso> listaActividades = new ArrayList<>();
Actividad actividadEntity;
List<Persona> listaPersonas;
public void delete(Mesa mesa) {
this.mesasRepository.deleteById(Integer.valueOf(mesa.getId()));
}
public List<Actividad> listarActiviades() {
return null;
}
public List<Persona> BuscarPersonasSinMesa(Mesa mesa) {
return this.personaRepo.buscarPersonaSinMesas(Integer.valueOf(mesa.getId()));
}
}
