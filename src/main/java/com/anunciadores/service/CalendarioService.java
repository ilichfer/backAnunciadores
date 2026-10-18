package com.anunciadores.service;

import com.anunciadores.model.Ministerio;
import com.anunciadores.model.Persona;
import com.anunciadores.model.PosicionesMinisterio;
import com.anunciadores.model.Servicio;
import com.anunciadores.repository.IMinisterioRepo;
import com.anunciadores.repository.IPersonaRepo;
import com.anunciadores.repository.IPosicionesRepo;
import com.anunciadores.repository.IServicioRepo;
import com.anunciadores.util.Fechas;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Calendario de turnos (personal o de toda la iglesia) y exportacion en formato iCalendar (.ics),
 * que Google Calendar puede importar o suscribir con un enlace privado.
 */
@Service
public class CalendarioService {

    private static final DateTimeFormatter ICS_FECHA = DateTimeFormatter.BASIC_ISO_DATE;
    private static final DateTimeFormatter ICS_STAMP = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
    private static final int MAX_DIAS_RANGO = 400;

    private final SecureRandom random = new SecureRandom();

    @Autowired
    private IServicioRepo servicioRepo;
    @Autowired
    private IPersonaRepo personaRepo;
    @Autowired
    private IMinisterioRepo ministerioRepo;
    @Autowired
    private IPosicionesRepo posicionesRepo;

    /**
     * Turnos entre dos fechas. Si idPersona es null se devuelven los de toda la iglesia.
     * El motivo de ausencia solo se incluye si incluirMotivos es true (admin o el propio servidor).
     */
    public List<Map<String, Object>> eventos(LocalDate desde, LocalDate hasta, Integer idPersona,
                                             Integer idUsuarioActual, boolean incluirMotivos) {
        if (hasta.isBefore(desde)) {
            hasta = desde;
        }
        if (desde.plusDays(MAX_DIAS_RANGO).isBefore(hasta)) {
            hasta = desde.plusDays(MAX_DIAS_RANGO);
        }
        List<Servicio> servicios = idPersona == null
                ? servicioRepo.findByRango(Fechas.aDate(desde), Fechas.aDate(hasta))
                : servicioRepo.findByPersonaYRango(idPersona, Fechas.aDate(desde), Fechas.aDate(hasta));

        Map<Integer, String> ministerios = new HashMap<>();
        for (Ministerio m : ministerioRepo.findAll()) {
            ministerios.put(m.getId(), m.getNombre());
        }
        Map<Integer, String> posiciones = new HashMap<>();
        for (PosicionesMinisterio p : posicionesRepo.findAll()) {
            posiciones.put(p.getId(), p.getNombrePosicion());
        }
        Map<Integer, String> personas = new HashMap<>();

        List<Map<String, Object>> eventos = new ArrayList<>();
        for (Servicio s : servicios) {
            String nombre = personas.computeIfAbsent(s.getIdPersona(), id -> personaRepo.findById(id)
                    .map(p -> p.getNombre() + " " + p.getApellido()).orElse("—"));
            boolean propio = idUsuarioActual != null && idUsuarioActual == s.getIdPersona();
            Map<String, Object> e = new LinkedHashMap<>();
            e.put("idServicio", s.getId());
            e.put("fecha", Fechas.aLocalDate(s.getFechaServicio()).toString());
            e.put("idMinisterio", s.getIdMinisterio());
            e.put("ministerio", ministerios.getOrDefault(s.getIdMinisterio(), "Ministerio"));
            e.put("posicion", posiciones.getOrDefault(s.getIdPosicion(), "Servicio"));
            e.put("idPersona", s.getIdPersona());
            e.put("persona", nombre);
            e.put("propio", propio);
            e.put("confirmacion", s.getConfirmacion());
            e.put("motivoConfirmacion", incluirMotivos || propio ? s.getMotivoConfirmacion() : null);
            e.put("asistencia", s.getAsistencia());
            eventos.add(e);
        }
        return eventos;
    }

    /** Devuelve (creandolo si no existe) el token secreto del calendario de la persona. */
    @Transactional
    public String tokenDe(Integer idPersona) {
        Persona persona = personaRepo.findById(idPersona).orElseThrow();
        if (persona.getCalendarioToken() == null) {
            persona.setCalendarioToken(nuevoToken());
            personaRepo.save(persona);
        }
        return persona.getCalendarioToken();
    }

    /** Cambia el token (el enlace anterior deja de funcionar). */
    @Transactional
    public String regenerarToken(Integer idPersona) {
        Persona persona = personaRepo.findById(idPersona).orElseThrow();
        persona.setCalendarioToken(nuevoToken());
        personaRepo.save(persona);
        return persona.getCalendarioToken();
    }

    public Optional<Persona> personaPorToken(String token) {
        if (token == null || token.length() < 30) {
            return Optional.empty();
        }
        return personaRepo.findByCalendarioToken(token);
    }

    /** Calendario .ics con los turnos de la persona: desde hace 2 meses hasta dentro de 6 meses. */
    public String icsPersona(Persona persona) {
        LocalDate hoy = Fechas.hoy();
        List<Map<String, Object>> eventos = eventos(hoy.minusMonths(2), hoy.plusMonths(6), persona.getId(),
                persona.getId(), true);
        return ics("Mis turnos - Anunciadores de Cristo", eventos);
    }

    public String ics(String nombreCalendario, List<Map<String, Object>> eventos) {
        String stamp = ZonedDateTime.now(ZoneOffset.UTC).format(ICS_STAMP);
        StringBuilder sb = new StringBuilder();
        linea(sb, "BEGIN:VCALENDAR");
        linea(sb, "VERSION:2.0");
        linea(sb, "PRODID:-//Anunciadores de Cristo//Turnos//ES");
        linea(sb, "CALSCALE:GREGORIAN");
        linea(sb, "METHOD:PUBLISH");
        linea(sb, "X-WR-CALNAME:" + escapar(nombreCalendario));
        linea(sb, "X-WR-TIMEZONE:America/Bogota");
        for (Map<String, Object> e : eventos) {
            LocalDate fecha = LocalDate.parse((String) e.get("fecha"));
            String estado = "NO_PUEDE".equals(e.get("confirmacion")) ? " (no puedo asistir)" : "";
            linea(sb, "BEGIN:VEVENT");
            linea(sb, "UID:servicio-" + e.get("idServicio") + "@anunciaig.com");
            linea(sb, "DTSTAMP:" + stamp);
            linea(sb, "DTSTART;VALUE=DATE:" + fecha.format(ICS_FECHA));
            linea(sb, "DTEND;VALUE=DATE:" + fecha.plusDays(1).format(ICS_FECHA));
            linea(sb, "SUMMARY:" + escapar(e.get("posicion") + " - " + e.get("ministerio") + estado));
            linea(sb, "DESCRIPTION:" + escapar("Servicio en " + e.get("ministerio") + " como " + e.get("posicion")
                    + ". Servidor: " + e.get("persona") + ". https://anunciaig.com/#/calendario"));
            linea(sb, "STATUS:" + ("NO_PUEDE".equals(e.get("confirmacion")) ? "CANCELLED" : "CONFIRMED"));
            linea(sb, "END:VEVENT");
        }
        linea(sb, "END:VCALENDAR");
        return sb.toString();
    }

    private String nuevoToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String escapar(String texto) {
        return texto.replace("\\", "\\\\").replace(";", "\\;").replace(",", "\\,").replace("\n", "\\n");
    }

    /** Agrega una linea respetando el limite de 75 caracteres del formato iCalendar. */
    private static void linea(StringBuilder sb, String contenido) {
        int max = 73;
        int i = 0;
        while (contenido.length() - i > max) {
            sb.append(contenido, i, i + max).append("\r\n ");
            i += max;
        }
        sb.append(contenido.substring(i)).append("\r\n");
    }
}
