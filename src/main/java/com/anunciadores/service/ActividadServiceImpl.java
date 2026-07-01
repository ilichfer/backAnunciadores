package  com.anunciadores.service;
import com.anunciadores.dto.ActividadDto;
import com.anunciadores.model.Actividad;
import com.anunciadores.model.Curso;
import com.anunciadores.model.Mesa;
import com.anunciadores.model.Persona;
import com.anunciadores.repository.IActividadRepo;
import com.anunciadores.repository.IPersonaRepo;
import com.anunciadores.service.interfaces.IActividadService;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ActividadServiceImpl
implements IActividadService
{
@Autowired
private IActividadRepo ActividadRepository;
@Autowired
private IPersonaRepo personaRepository;
List<Curso> listaActividades = new ArrayList<>();
Actividad actividadEntity;
List<Persona> listaPersonas;
public List<Actividad> listarActiviades() {
return this.ActividadRepository.findAll();
}
public Actividad save(ActividadDto actividadDTO) throws ParseException {
this.actividadEntity = new Actividad();
this.actividadEntity.setNombreActividad(actividadDTO.getNombreActividad());
this.actividadEntity.setFecha(actividadDTO.getFecha());
if (actividadDTO.getId() != 0) {
this.actividadEntity.setId(actividadDTO.getId());
}
return (Actividad)this.ActividadRepository.save(this.actividadEntity);
}
public Actividad findActividadById(Integer id) {
return this.ActividadRepository.findById(id).get();
}
public void delete(ActividadDto actividadDTO) {
this.ActividadRepository.deleteById(Integer.valueOf(actividadDTO.getId()));
}
public List<Persona> buscarTodosSinActividad(int idActividad) {
return this.personaRepository.buscarPersonaSinActividad(Integer.valueOf(idActividad));
}
public List<Persona> buscarPersonasActividad(int idActividad) {
return this.personaRepository.buscarPersonaByActividad(Integer.valueOf(idActividad));
}
public List<Mesa> buscarMesasActividad(ActividadDto actividadDto) {
return null;
}
public List<Actividad> listarActiviadesByPersona(int idPersona) {
return this.ActividadRepository.findActividadesByPersona(idPersona);
}
}
