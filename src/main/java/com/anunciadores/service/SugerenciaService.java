package  com.anunciadores.service;
import com.anunciadores.model.Sugerencia;
import com.anunciadores.repository.IPersonaRepo;
import com.anunciadores.repository.ISugerenciaRepo;
import com.anunciadores.service.interfaces.ISugerenciaService;
import org.springframework.stereotype.Service;
@Service
public class SugerenciaService implements ISugerenciaService {
private ISugerenciaRepo sugerenciaRepo;
private IPersonaRepo personaRepo;
public SugerenciaService(ISugerenciaRepo sugerenciaRepo, IPersonaRepo personaRepo) {
this.sugerenciaRepo = sugerenciaRepo;
this.personaRepo = personaRepo;
}
public Sugerencia saveSugerencia(Sugerencia saveSugerencia, Integer idPersona) {
saveSugerencia.setPersona(this.personaRepo.findById(idPersona).get());
return (Sugerencia)this.sugerenciaRepo.save(saveSugerencia);
}
}
