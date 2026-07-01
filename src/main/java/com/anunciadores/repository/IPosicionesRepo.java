package  com.anunciadores.repository;
import com.anunciadores.model.PosicionesMinisterio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface IPosicionesRepo extends JpaRepository<PosicionesMinisterio, Integer> {
  @Modifying
  @Query("select p from PosicionesMinisterio p  where p.idMinisterio = :idMinisterio")
  List<PosicionesMinisterio> findAllByIdMinisterio(@Param("idMinisterio") int paramInt);
  @Modifying
  @Query("select p from PosicionesMinisterio p  where p.nombrePosicion = :nombrePosicion and p.idMinisterio =:idMinisterio")
  List<PosicionesMinisterio> findMinisterioByName(@Param("nombrePosicion") String paramString, @Param("idMinisterio") int paramInt);
  @Query("select p from PosicionesMinisterio p  where p.id = :idPosicion and p.idMinisterio =:idMinisterio")
  PosicionesMinisterio findMinisterioByName(@Param("idPosicion") int paramInt1, @Param("idMinisterio") int paramInt2);
}
