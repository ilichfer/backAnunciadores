package  com.anunciadores.repository;
import com.anunciadores.model.Tdc;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface ITdcRepo extends JpaRepository<Tdc, Integer> {
  @Modifying
  @Query("select u from Tdc u  where u.fechaCreacion = :date")
  List<Tdc> findAllByDate(@Param("date") Date paramDate);
  @Modifying
  @Query("select u from Tdc u  where u.fechaCreacion = :date and u.idPersona = :idPersona")
  List<Tdc> findAllByDateAndPersona(@Param("date") Date paramDate, @Param("idPersona") int paramInt);
  @Modifying
  @Query("select CONCAT(p.nombre ,' ', p.apellido)  , COUNT(t.idPersona), t.idPersona from Tdc t join Persona p on t.idPersona  = p.id where t.fechaCreacion BETWEEN :dateStart and :dateEnd GROUP BY t.idPersona ORDER by p.nombre asc ")
  List<Object> findAllBetweenDates(@Param("dateStart") Date paramDate1, @Param("dateEnd") Date paramDate2);
  @Query("select CONCAT(p.nombre ,' ', p.apellido)  , COUNT(t.idPersona), t.idPersona from Tdc t join Persona p on t.idPersona  = p.id where t.fechaCreacion BETWEEN :dateStart and :dateEnd and t.idPersona = :idPersona ")
  Optional<Object> findAllBetweenDatesAndPerson(@Param("dateStart") Date paramDate1, @Param("dateEnd") Date paramDate2, @Param("idPersona") Integer paramInteger);
  @Modifying
  @Query("select t from Tdc t join Persona p on t.idPersona  = p.id where t.fechaCreacion BETWEEN :dateStart and :dateEnd and p.id = :idPersona order by t.fechaCreacion desc")
  List<Tdc> findAllBetweenDatesByPersona(@Param("dateStart") Date paramDate1, @Param("dateEnd") Date paramDate2, @Param("idPersona") int paramInt);

  /** Dias (sin repetir) en que la persona subio su TCD, del mas reciente al mas antiguo. */
  @Query("select distinct t.fechaCreacion from Tdc t where t.idPersona = :idPersona order by t.fechaCreacion desc")
  List<Date> findFechasByPersona(@Param("idPersona") int idPersona);
  /** Personas que subieron al menos un TCD en el rango. */
  @Query("select distinct t.idPersona from Tdc t where t.fechaCreacion between :desde and :hasta")
  List<Integer> findPersonasConTcd(@Param("desde") Date desde, @Param("hasta") Date hasta);
  /** Pares (idPersona, fecha) distintos de TCD en el rango: un TCD por persona por dia. */
  @Query("select distinct t.idPersona, t.fechaCreacion from Tdc t where t.fechaCreacion between :desde and :hasta")
  List<Object[]> findDiasTcd(@Param("desde") Date desde, @Param("hasta") Date hasta);
}
