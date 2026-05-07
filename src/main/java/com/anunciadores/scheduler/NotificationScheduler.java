package com.anunciadores.scheduler;

import com.anunciadores.model.Notificacion;
import com.anunciadores.model.Persona;
import com.anunciadores.repository.INotificacionRepo;
import com.anunciadores.repository.IPersonaRepo;
import com.anunciadores.service.interfaces.INotificacionService;
import com.anunciadores.util.UtilDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {
    private static final Logger log = LoggerFactory.getLogger(NotificationScheduler.class);

    @Autowired
    private INotificacionService notificacionService;

    @Autowired
    private IPersonaRepo personaRepo;

    @Autowired
    private UtilDate utilDate;

    /**
     * Recordatorio para servicios del día siguiente.
     * Se ejecuta automáticamente a las 8:00 AM todos los días.
     *
     * Flujo:
     * 1. Calcula la fecha de mañana
     * 2. Busca notificaciones pendientes para esa fecha (enviada = false)
     * 3. Para cada notificación, verifica si la persona tiene idTelegram configurado
     * 4. Si tiene Telegram, simulation de envío de mensaje (log)
     * 5. Marca la notificación como enviada = true
     *
     * NOTA: La integración real con Telegram se haría aquí:
     * - Usar el servicio SenNotificationTelegram o TelegramBot
     * - Enviar mensaje al chat_id registrado en persona.getIdTelegram()
     * - Por ahora solo se logged el mensaje que se enviaría
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void enviarRecordatoriosManana() {
        log.info("Iniciando envío de recordatorios para mañana...");
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date fechaManana = cal.getTime();

            List<Notificacion> notificaciones = notificacionService.getNotificacionesPendientesPorFecha(fechaManana);
            for (Notificacion n : notificaciones) {
                Persona persona = personaRepo.findById(n.getIdPersona()).orElse(null);

                // Solo envía si la persona tiene idTelegram configurado
                if (persona != null && persona.getIdTelegram() != null && !persona.getIdTelegram().isEmpty()) {
                    String mensaje = "⏰ RECORDATORIO: Tienes servicio mañana " +
                        formatFecha(fechaManana) + " como " + n.getPosicion() +
                        " en " + n.getMinisterio();

                    // // Aquí iría la integración real con Telegram:
                    // // telegramService.enviarMensaje(persona.getIdTelegram(), mensaje);
                    // log.info("Enviando recordatorio Telegram a {}: {}", persona.getNombre(), mensaje);

                    // Marcar como enviada para no volver a enviar
                    n.setEnviada(true);
                    notificacionService.marcarEnviada(n.getId());
                }
            }
            log.info("Recordatorios para mañana enviados: {}", notificaciones.size());
        } catch (Exception e) {
            log.error("Error al enviar recordatorios: ", e);
        }
    }

    /**
     * Notificación para servicios del día actual.
     * Se ejecuta automáticamente a las 9:00 AM todos los días.
     *
     * Flujo:
     * 1. Obtiene la fecha actual
     * 2. Busca notificaciones pendientes para hoy (enviada = false)
     * 3. Para cada notificación, verifica si la persona tiene idTelegram
     * 4. Si tiene Telegram, simulation de envío de mensaje
     * 5. Marca la notificación como enviada = true
     *
     * NOTA: Esta función complementa la anterior.
     * - A las 8 AM: Recordatorio para mañana
     * - A las 9 AM: Recordatorio para hoy (para quienes no recibieron el de ayer)
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void enviarNotificacionesDia() {
        log.info("Iniciando notificaciones para hoy...");
        try {
            Date fechaHoy = new Date();
            List<Notificacion> notificaciones = notificacionService.getNotificacionesPendientesPorFecha(fechaHoy);
            for (Notificacion n : notificaciones) {
                Persona persona = personaRepo.findById(n.getIdPersona()).orElse(null);

                // Solo envía si la persona tiene idTelegram configurado
                if (persona != null && persona.getIdTelegram() != null && !persona.getIdTelegram().isEmpty()) {
                    String mensaje = "🔔 TU SERVICIO HOY: " + n.getPosicion() +
                        " en " + n.getMinisterio();

                    // // Aquí iría la integración real con Telegram:
                    // // telegramService.enviarMensaje(persona.getIdTelegram(), mensaje);
                    // log.info("Enviando notificación Telegram a {}: {}", persona.getNombre(), mensaje);

                    // Marcar como enviada para no volver a enviar
                    n.setEnviada(true);
                    notificacionService.marcarEnviada(n.getId());
                }
            }
            log.info("Notificaciones del día enviadas: {}", notificaciones.size());
        } catch (Exception e) {
            log.error("Error al enviar notificaciones del día: ", e);
        }
    }

    private String formatFecha(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        String[] meses = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
        return dia + " " + meses[cal.get(Calendar.MONTH)];
    }
}