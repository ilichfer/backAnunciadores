package  com.anunciadores.repository;
import com.anunciadores.model.Curso;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
@Service
public class CursosRepoImpl
{
@Autowired
JdbcTemplate jdbcTemplate;
public List<Curso> cursosByIdPersona(Integer idPersona) {
StringBuilder sql = new StringBuilder();
Curso retorno = new Curso();
List<Curso> cuersosList = new ArrayList<>();
try {
sql.append("SELECT c.* FROM persona p   join inscripciones ins on p.id = ins.id_persona join curso c on ins.id_curso = c.id  where p.id= " + idPersona);
retorno = (Curso)this.jdbcTemplate.query(sql.toString(), (ResultSetExtractor)new Object());
}
catch (Exception e) {
e.printStackTrace();
} 
return cuersosList;
}
}
