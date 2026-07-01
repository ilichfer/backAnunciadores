package com.anunciadores.service;
import com.anunciadores.model.Notificacion;
import com.anunciadores.repository.INotificacionRepo;
import com.anunciadores.service.interfaces.INotificacionService;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class NotificacionServiceImpl implements INotificacionService {
    private final Logger log = LoggerFactory.getLogger(NotificacionServiceImpl.class);
    @Autowired
    private INotificacionRepo notificacionRepo;
    @Override
    @Transactional
    public Notificacion crearNotificacion(Notificacion notificacion) {
        try {
            return notificacionRepo.save(notificacion);
        } catch (Exception e) {
            log.error("Error al crear notificación: ", e);
            return null;
        }
    }
    @Override
    public List<Notificacion> getNotificacionesPorPersona(Integer idPersona) {
        return notificacionRepo.findByIdPersonaOrderByFechaCreacionDesc(idPersona);
    }
    @Override
    public List<Notificacion> getNotificacionesNoLeidas(Integer idPersona) {
        return notificacionRepo.findByIdPersonaAndLeidaFalseOrderByFechaCreacionDesc(idPersona);
    }
    @Override
    public Integer countNotificacionesNoLeidas(Integer idPersona) {
        return notificacionRepo.countNoLeidas(idPersona);
    }
    @Override
    @Transactional
    public Notificacion marcarLeida(Integer id) {
        Optional<Notificacion> optional = notificacionRepo.findById(id);
        if (optional.isPresent()) {
            Notificacion notificacion = optional.get();
            notificacion.setLeida(true);
            return notificacionRepo.save(notificacion);
        }
        return null;
    }
    @Override
    @Transactional
    public void marcarTodasLeidas(Integer idPersona) {
        List<Notificacion> notificaciones = getNotificacionesNoLeidas(idPersona);
        for (Notificacion n : notificaciones) {
            n.setLeida(true);
            notificacionRepo.save(n);
        }
    }
    @Override
    @Transactional
    public void eliminarNotificacionesAntiguas(Integer idPersona, int dias) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -dias);
        Date fechaLimite = cal.getTime();
        List<Notificacion> antiguas = notificacionRepo.findByIdPersonaAndFechaCreacionBefore(idPersona, fechaLimite);
        if (!antiguas.isEmpty()) {
            notificacionRepo.deleteAll(antiguas);
        }
    }
    @Override
    public List<Notificacion> getNotificacionesPendientesPorFecha(Date fecha) {
        return notificacionRepo.findByFechaServicioAndEnviadaFalse(fecha);
    }
    @Override
    @Transactional
    public void marcarEnviada(Integer id) {
        Optional<Notificacion> optional = notificacionRepo.findById(id);
        if (optional.isPresent()) {
            Notificacion notificacion = optional.get();
            notificacion.setEnviada(true);
            notificacionRepo.save(notificacion);
        }
    }
    @Override
    @Transactional
    public void crearNotificacionAsignacion(Integer idPersona, Date fechaServicio, Integer idMinisterio, String ministerio, String posicion) {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdPersona(idPersona);
        notificacion.setTipo("ASIGNACION");
        notificacion.setTitulo("Nuevo servicio asignado");
        notificacion.setMensaje("Has sido asignado en la posicion de " + posicion + " en " + ministerio);
        notificacion.setFechaServicio(fechaServicio);
        notificacion.setIdMinisterio(idMinisterio);
        notificacion.setMinisterio(ministerio);
        notificacion.setPosicion(posicion);
        notificacion.setLeida(false);
        notificacion.setEnviada(false);
        notificacion.setFechaCreacion(new Date());
        notificacionRepo.save(notificacion);
    }
}