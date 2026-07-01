package  com.anunciadores.repository;
import com.anunciadores.model.Persona;
import com.anunciadores.repository.IPersonaRepo;
import com.anunciadores.repository.InscripcionActividadRepo;
import com.anunciadores.repository.InscripcionRepo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
@Service
public class MesasRepoImpl
{
@Autowired
JdbcTemplate jdbcTemplate;
@Autowired
private IPersonaRepo PersonaRepository;
@Autowired
private InscripcionRepo inscripcionesRepository;
@Autowired
private InscripcionActividadRepo inscripcionActividadRepository;
public List<Persona> buscarPersonaSinMesas1(Integer idMesa) {
StringBuilder sql = new StringBuilder();
Persona retorno = new Persona();
List<Persona> personaList = new ArrayList<>();
try {
sql.append("select * from persona p where id not in (select pm.id_persona from mesa  m  join persona_mesa pm on m.id = pm.id_mesa where m.id =" + idMesa + ")");
retorno = (Persona)this.jdbcTemplate.query(sql.toString(), (ResultSetExtractor)new Object());
}
catch (Exception e) {
e.printStackTrace();
} 
return personaList;
}
}
