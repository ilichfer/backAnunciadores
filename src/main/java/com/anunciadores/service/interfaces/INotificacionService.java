package com.anunciadores.service.interfaces;
import com.anunciadores.model.Notificacion;
import java.util.Date;
import java.util.List;
public interface INotificacionService {
    Notificacion crearNotificacion(Notificacion notificacion);
    List<Notificacion> getNotificacionesPorPersona(Integer idPersona);
    List<Notificacion> getNotificacionesNoLeidas(Integer idPersona);
    Integer countNotificacionesNoLeidas(Integer idPersona);
    Notificacion marcarLeida(Integer id);
    void marcarTodasLeidas(Integer idPersona);
    void eliminarNotificacionesAntiguas(Integer idPersona, int dias);
    List<Notificacion> getNotificacionesPendientesPorFecha(Date fecha);
    void marcarEnviada(Integer id);
    void crearNotificacionAsignacion(Integer idPersona, Date fechaServicio, Integer idMinisterio, String ministerio, String posicion);
}