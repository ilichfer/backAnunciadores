package  com.anunciadores.repository;
import com.anunciadores.model.Mensajes;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface IMensajesRepo extends JpaRepository<Mensajes, Integer> {
  @Query("select m from Mensajes m where m.remitente.id = :idPersona and m.activo = true ")
  List<Mensajes> findByIdPersona(@Param("idPersona")int paramInt);
  @Query("select m from Mensajes m where m.destinatario.id = :idPersona")
  List<Mensajes> findMensajesByIdPersona(@Param("idPersona")int paramInt);
  @Query("select m from Mensajes m where m.destinatario.id = :idPersona and m.activo = true ")
  List<Mensajes> mesajesSinLeerByIdPersona(@Param("idPersona")int paramInt);
}
