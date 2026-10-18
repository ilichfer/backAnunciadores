package com.anunciadores.service;

import com.anunciadores.model.Coordinador;
import com.anunciadores.model.Ministerio;
import com.anunciadores.model.Persona;
import com.anunciadores.model.PosicionesMinisterio;
import com.anunciadores.model.Rol;
import com.anunciadores.model.Servicio;
import com.anunciadores.repository.IMinisterioRepo;
import com.anunciadores.repository.IPersonaRepo;
import com.anunciadores.repository.IPosicionesRepo;
import com.anunciadores.repository.IServicioRepo;
import com.anunciadores.security.CurrentUser;
import com.anunciadores.service.interfaces.IServicioService;
import com.anunciadores.util.Fechas;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * El servidor confirma su turno o avisa que no puede; en ese caso se avisa al coordinador del dia
 * y a los administradores.
 */
@Service
public class ConfirmacionTurnoService {

    public static final String CONFIRMADO = "CONFIRMADO";
    public static final String NO_PUEDE = "NO_PUEDE";

    @Autowired
    private IServicioRepo servicioRepo;
    @Autowired
    private IPersonaRepo personaRepo;
    @Autowired
    private IMinisterioRepo ministerioRepo;
    @Autowired
    private IPosicionesRepo posicionesRepo;
    @Autowired
    private IServicioService servicioService;
    @Autowired
    private NotificadorService notificador;
    @Autowired
    private AuditoriaService auditoria;

    @Transactional
    public Map<String, Object> responder(int idServicio, String estado, String motivo) {
        if (!CONFIRMADO.equals(estado) && !NO_PUEDE.equals(estado)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La respuesta debe ser CONFIRMADO o NO_PUEDE");
        }
        Servicio servicio = servicioRepo.findById(idServicio).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Turno no encontrado"));
        CurrentUser.requireSelfOrAdmin(servicio.getIdPersona());

        LocalDate fecha = Fechas.aLocalDate(servicio.getFechaServicio());
        if (fecha != null && fecha.isBefore(Fechas.hoy())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este turno ya pasó");
        }

        String anterior = servicio.getConfirmacion();
        String motivoLimpio = motivo == null ? null : motivo.trim();
        if (motivoLimpio != null && motivoLimpio.length() > 300) {
            motivoLimpio = motivoLimpio.substring(0, 300);
        }
        servicio.setConfirmacion(estado);
        servicio.setMotivoConfirmacion(NO_PUEDE.equals(estado) ? motivoLimpio : null);
        servicio.setFechaConfirmacion(new Date());
        servicioRepo.save(servicio);

        Persona persona = personaRepo.findById(servicio.getIdPersona()).orElse(null);
        String nombre = persona != null ? persona.getNombre() + " " + persona.getApellido() : "Un servidor";
        String ministerio = ministerioRepo.findById(servicio.getIdMinisterio()).map(Ministerio::getNombre).orElse("ministerio");
        String posicion = posicionesRepo.findById(servicio.getIdPosicion())
                .map(PosicionesMinisterio::getNombrePosicion).orElse("servicio");
        String detalle = posicion + " en " + ministerio + " el " + Fechas.legible(fecha);

        auditoria.registrar(AuditoriaService.CONFIRMACION_TURNO, "servicio", idServicio,
                nombre + (NO_PUEDE.equals(estado) ? " NO PUEDE servir: " : " confirmó: ") + detalle
                        + (motivoLimpio != null && NO_PUEDE.equals(estado) ? " (motivo: " + motivoLimpio + ")" : ""));

        if (NO_PUEDE.equals(estado) && !NO_PUEDE.equals(anterior)) {
            String mensaje = nombre + " avisó que no puede servir como " + detalle
                    + (motivoLimpio != null && !motivoLimpio.isEmpty() ? ". Motivo: " + motivoLimpio : "")
                    + ". Hay que buscar reemplazo.";
            avisarResponsables(servicio, "⚠️ " + nombre + " no puede servir", mensaje, true);
        } else if (CONFIRMADO.equals(estado) && NO_PUEDE.equals(anterior)) {
            avisarResponsables(servicio, "✅ " + nombre + " ya puede servir",
                    nombre + " confirmó que sí podrá servir como " + detalle + ".", false);
        }

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("idServicio", idServicio);
        resultado.put("confirmacion", estado);
        resultado.put("motivoConfirmacion", servicio.getMotivoConfirmacion());
        return resultado;
    }

    /** Coordinador del dia + administradores (sin repetir y sin avisarle a quien respondio). */
    private void avisarResponsables(Servicio servicio, String titulo, String mensaje, boolean porWhatsapp) {
        Set<Persona> destinatarios = new LinkedHashSet<>();
        Coordinador coordinador = servicioService.findCoordinadorByFecha(servicio.getFechaServicio());
        if (coordinador != null && coordinador.getPersona() != null) {
            destinatarios.add(coordinador.getPersona());
        }
        destinatarios.addAll(personaRepo.findActivasPorRol(Rol.ID_ADMIN));

        Set<Integer> avisados = new LinkedHashSet<>();
        for (Persona p : destinatarios) {
            if (p.getId() == null || p.getId() == servicio.getIdPersona() || !avisados.add(p.getId())) {
                continue;
            }
            notificador.notificar(p.getId(), "AUSENCIA", titulo, mensaje);
        }
        if (porWhatsapp && coordinador != null && coordinador.getPersona() != null
                && coordinador.getPersona().getId() != servicio.getIdPersona()) {
            notificador.whatsappAsync(coordinador.getPersona(), titulo + "\n\n" + mensaje,
                    ConfiguracionServiceImpl.WA_AUSENCIAS);
        }
    }
}
