package com.anunciadores.scheduler;

import com.anunciadores.model.EnvioAutomatico;
import com.anunciadores.model.Persona;
import com.anunciadores.model.Rol;
import com.anunciadores.repository.IEnvioAutomaticoRepo;
import com.anunciadores.repository.IPersonaRepo;
import com.anunciadores.service.ConfiguracionServiceImpl;
import com.anunciadores.service.NotificadorService;
import com.anunciadores.service.PanelPastorService;
import com.anunciadores.service.ResumenSemanalService;
import com.anunciadores.util.Fechas;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Envios automaticos (hora de Bogota):
 * - 08:00 saludo de cumpleanos
 * - 19:00 recordatorio de TCD a quien aun no lo sube
 * - lunes 07:00 resumen semanal para los administradores
 *
 * Siempre se crea la notificacion en la app y el push; WhatsApp solo si el admin activo ese envio.
 * Cada envio se registra en envio_automatico para no repetirlo el mismo dia.
 */
@Component
public class TareasAutomaticasScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TareasAutomaticasScheduler.class);

    static final String CUMPLEANOS = "CUMPLEANOS";
    static final String CUMPLEANOS_ADMIN = "CUMPLEANOS_ADMIN";
    static final String TCD_RECORDATORIO = "TCD_RECORDATORIO";

    @Autowired
    private IPersonaRepo personaRepo;
    @Autowired
    private IEnvioAutomaticoRepo envioRepo;
    @Autowired
    private NotificadorService notificador;
    @Autowired
    private PanelPastorService panel;
    @Autowired
    private ResumenSemanalService resumenSemanal;

    @Scheduled(cron = "${tareas.cumpleanos.cron:0 0 8 * * *}", zone = "America/Bogota")
    public void saludarCumpleaneros() {
        LocalDate hoy = Fechas.hoy();
        try {
            List<Persona> cumpleaneros = personaRepo.findCumpleanerosDelDia(hoy.getMonthValue(), hoy.getDayOfMonth());
            for (Persona p : cumpleaneros) {
                if (!marcar(CUMPLEANOS, p.getId(), hoy)) {
                    continue;
                }
                notificador.notificar(p.getId(), "CUMPLEANOS", "🎂 ¡Feliz cumpleaños, " + p.getNombre() + "!",
                        "Toda la familia Anunciadores de Cristo celebra tu vida hoy. ¡Dios te bendiga!");
                notificador.whatsapp(p, mensajeCumpleanos(p), ConfiguracionServiceImpl.WA_CUMPLEANOS);
            }
            if (!cumpleaneros.isEmpty()) {
                String nombres = cumpleaneros.stream().map(p -> p.getNombre() + " " + p.getApellido())
                        .collect(Collectors.joining(", "));
                for (Persona admin : personaRepo.findActivasPorRol(Rol.ID_ADMIN)) {
                    if (marcar(CUMPLEANOS_ADMIN, admin.getId(), hoy)) {
                        notificador.notificar(admin.getId(), "CUMPLEANOS", "🎂 Hoy cumplen años", nombres);
                    }
                }
            }
            LOGGER.info("Cumpleaños de hoy: {}", cumpleaneros.size());
        } catch (Exception e) {
            LOGGER.error("Error en el saludo de cumpleaños", e);
        }
    }

    @Scheduled(cron = "${tareas.recordatorio-tcd.cron:0 0 19 * * *}", zone = "America/Bogota")
    public void recordarTcd() {
        LocalDate hoy = Fechas.hoy();
        try {
            // Solo a quienes participan del TCD (subieron alguno en los ultimos 30 dias)
            Set<Integer> participantes = panel.participantesTcd(hoy);
            Set<Integer> yaSubieron = panel.personasConTcd(hoy, hoy);
            int enviados = 0;
            for (Integer id : participantes) {
                if (yaSubieron.contains(id)) {
                    continue;
                }
                Persona p = personaRepo.findById(id).orElse(null);
                if (p == null || !Boolean.TRUE.equals(p.getEstado()) || !marcar(TCD_RECORDATORIO, id, hoy)) {
                    continue;
                }
                notificador.notificar(id, "TCD_RECORDATORIO", "🙏 ¿Ya tuviste tu Tiempo Con Dios?",
                        "Aún no has subido tu TCD de hoy. ¡No pierdas tu racha!");
                notificador.whatsapp(p, "Hola " + p.getNombre() + " 🙏\nAún no has subido tu Tiempo Con Dios de hoy. "
                        + "¡No pierdas tu racha!\nhttps://anunciaig.com/#/tcd", ConfiguracionServiceImpl.WA_RECORDATORIO_TCD);
                enviados++;
            }
            LOGGER.info("Recordatorios de TCD enviados: {}", enviados);
        } catch (Exception e) {
            LOGGER.error("Error en el recordatorio de TCD", e);
        }
    }

    @Scheduled(cron = "${tareas.resumen-semanal.cron:0 0 7 * * MON}", zone = "America/Bogota")
    public void enviarResumenSemanal() {
        try {
            resumenSemanal.generarYEnviar(Fechas.hoy());
        } catch (Exception e) {
            LOGGER.error("Error al generar el resumen semanal", e);
        }
    }

    /** Registra el envio; devuelve false si ya se habia hecho hoy (no se repite). */
    private boolean marcar(String tipo, Integer idPersona, LocalDate dia) {
        Date fecha = Fechas.aDate(dia);
        if (envioRepo.existsByTipoAndIdPersonaAndFecha(tipo, idPersona, fecha)) {
            return false;
        }
        try {
            envioRepo.save(new EnvioAutomatico(tipo, idPersona, fecha));
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    private static String mensajeCumpleanos(Persona p) {
        return "🎉 ¡Feliz cumpleaños, " + p.getNombre() + "! 🎂\n\n"
                + "Toda la familia de *Anunciadores de Cristo* da gracias a Dios por tu vida. "
                + "Que este nuevo año esté lleno de su favor, su paz y su misericordia.\n\n"
                + "\"Jehová te bendiga, y te guarde\" — Números 6:24 🙏";
    }
}
