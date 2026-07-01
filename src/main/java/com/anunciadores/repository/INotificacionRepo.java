package com.anunciadores.repository;
import com.anunciadores.model.Notificacion;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface INotificacionRepo extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findByIdPersonaOrderByFechaCreacionDesc(Integer idPersona);
    List<Notificacion> findByIdPersonaAndLeidaFalseOrderByFechaCreacionDesc(Integer idPersona);
    @Query("SELECT COUNT(n) FROM Notificacion n WHERE n.idPersona = :idPersona AND n.leida = false")
    Integer countNoLeidas(@Param("idPersona") Integer idPersona);
    List<Notificacion> findByFechaServicioAndEnviadaFalse(Date fechaServicio);
    List<Notificacion> findByIdPersonaAndFechaCreacionBefore(Integer idPersona, Date fechaLimite);
}