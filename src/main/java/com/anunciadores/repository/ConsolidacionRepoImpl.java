  package  com.anunciadores.repository;
  import com.anunciadores.model.Persona;
  import com.anunciadores.model.inscripcionConsolidacion;
  import java.util.ArrayList;
  import java.util.List;
  import javax.persistence.EntityManager;
  import javax.persistence.PersistenceContext;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.jdbc.core.JdbcTemplate;
  import org.springframework.jdbc.core.ResultSetExtractor;
  import org.springframework.stereotype.Service;
  @Service
  public class ConsolidacionRepoImpl
  {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;
    public List<Persona> listarConsolidacion1() {
StringBuilder sql = new StringBuilder();
Persona retorno = new Persona();
List<Persona> personaList = new ArrayList<>();
      try {
sql.append("select * from persona p ");
sql.append("join consolidacion c on  p.id <> c.id_persona_consolidar ");
sql.append("WHERE p.id not in(select pr.id_persona from persona_rol pr) ");
System.out.println("buscar personas sin consolidacion ===> " + sql.toString());
retorno = (Persona)this.jdbcTemplate.query(sql.toString(), (ResultSetExtractor)new Object());
      }
catch (Exception e) {
e.printStackTrace();
      } 
return personaList;
    }
    public inscripcionConsolidacion listarConsolidacionByPersona(int idPersona) {
StringBuilder sql = new StringBuilder();
sql.append("select * from consolidacion c ");
sql.append("WHERE c.id_persona = " + idPersona);
      try {
return (inscripcionConsolidacion)this.entityManager.createNativeQuery(sql.toString(), inscripcionConsolidacion.class)
.getSingleResult();
} catch (Exception e) {
e.printStackTrace();
return null;
      } 
    }
    public Persona listarPersonaConsolidacion(int idPersona) {
StringBuilder sql = new StringBuilder();
sql.append("select p.* from consolidacion c ");
sql.append("join persona p on  c.id_persona_consolidar = p.id ");
sql.append("WHERE c.id_persona = " + idPersona);
      try {
return (Persona)this.entityManager.createNativeQuery(sql.toString(), Persona.class)
.getSingleResult();
} catch (Exception e) {
e.printStackTrace();
return null;
      } 
    }
  }
