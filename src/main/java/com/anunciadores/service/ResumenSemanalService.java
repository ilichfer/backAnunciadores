package com.anunciadores.service;

import com.anunciadores.model.Persona;
import com.anunciadores.model.ResumenSemanal;
import com.anunciadores.model.Rol;
import com.anunciadores.model.Servicio;
import com.anunciadores.repository.IPersonaRepo;
import com.anunciadores.repository.IResumenSemanalRepo;
import com.anunciadores.util.Fechas;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Resumen semanal para el pastor: quien falta (TCD y turnos), tendencias frente a la semana anterior,
 * cumpleanos de la semana que empieza y personas nuevas.
 */
@Service
public class ResumenSemanalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResumenSemanalService.class);

    @Autowired
    private PanelPastorService panel;
    @Autowired
    private IResumenSemanalRepo resumenRepo;
    @Autowired
    private IPersonaRepo personaRepo;
    @Autowired
    private NotificadorService notificador;

    private final ObjectMapper mapper = new ObjectMapper();

    /** Lunes de la semana anterior a la fecha dada (la semana que se resume). */
    public static LocalDate inicioSemanaAnterior(LocalDate fecha) {
        return fecha.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1);
    }

    /** Genera (o regenera) el resumen de la semana que empieza el lunes indicado. */
    @Transactional
    public Map<String, Object> generar(LocalDate lunes) {
        LocalDate domingo = lunes.plusDays(6);
        Map<Integer, Persona> porId = panel.personasPorId();

        // TCD: semana resumida vs. semana anterior
        int participantes = panel.participantesTcd(domingo).size();
        int tcdSemana = panel.porcentajeTcd(lunes, domingo, participantes);
        int tcdAnterior = panel.porcentajeTcd(lunes.minusWeeks(1), domingo.minusWeeks(1),
                panel.participantesTcd(domingo.minusWeeks(1)).size());
        Set<Integer> conTcd = panel.personasConTcd(lunes, domingo);
        List<Map<String, Object>> sinTcd = panel.participantesTcd(domingo).stream()
                .filter(id -> !conTcd.contains(id))
                .map(porId::get)
                .filter(p -> p != null && Boolean.TRUE.equals(p.getEstado()))
                .sorted(Comparator.comparing(Persona::getNombre, String.CASE_INSENSITIVE_ORDER))
                .map(PanelPastorService::personaCorta)
                .collect(Collectors.toList());

        // Turnos: asistencia de la semana resumida y ausencias
        Map<String, Object> turnos = panel.conteoTurnos(lunes, domingo);
        Map<String, Object> turnosAnterior = panel.conteoTurnos(lunes.minusWeeks(1), domingo.minusWeeks(1));
        List<Map<String, Object>> faltaron = new ArrayList<>();
        for (Servicio s : panel.turnos(lunes, domingo)) {
            boolean ausente = "AUSENTE".equalsIgnoreCase(s.getAsistencia());
            if (ausente || "NO_PUEDE".equals(s.getConfirmacion())) {
                Persona p = porId.get(s.getIdPersona());
                if (p == null) {
                    continue;
                }
                Map<String, Object> f = PanelPastorService.personaCorta(p);
                f.put("fecha", Fechas.aLocalDate(s.getFechaServicio()).toString());
                f.put("tipo", ausente ? "AUSENTE" : "AVISO_NO_PUEDE");
                f.put("motivo", s.getMotivoConfirmacion());
                faltaron.add(f);
            }
        }

        // Semana que empieza: turnos sin confirmar y cumpleanos
        LocalDate proximoLunes = lunes.plusWeeks(1);
        Map<String, Object> proximaSemana = panel.conteoTurnos(proximoLunes, proximoLunes.plusDays(6));
        List<Map<String, Object>> cumpleanos = panel.cumpleanos(panel.activas(), proximoLunes, 7);

        // Personas nuevas registradas en la semana resumida
        List<Map<String, Object>> nuevos = porId.values().stream()
                .filter(p -> p.getFechaRegistro() != null)
                .filter(p -> {
                    LocalDate d = Fechas.aLocalDate(p.getFechaRegistro());
                    return !d.isBefore(lunes) && !d.isAfter(domingo);
                })
                .map(PanelPastorService::personaCorta)
                .collect(Collectors.toList());

        Map<String, Object> r = new LinkedHashMap<>();
        r.put("semanaInicio", lunes.toString());
        r.put("semanaFin", domingo.toString());
        r.put("tcdPorcentaje", tcdSemana);
        r.put("tcdPorcentajeAnterior", tcdAnterior);
        r.put("tcdParticipantes", participantes);
        r.put("sinTcd", sinTcd);
        r.put("asistencia", turnos);
        r.put("asistenciaAnterior", turnosAnterior);
        r.put("faltaron", faltaron);
        r.put("proximaSemana", proximaSemana);
        r.put("cumpleanos", cumpleanos);
        r.put("nuevos", nuevos);
        r.put("texto", texto(r));

        guardar(lunes, domingo, r);
        return r;
    }

    /** Genera el resumen de la semana pasada y lo envia a los administradores. */
    public void generarYEnviar(LocalDate hoy) {
        Map<String, Object> r = generar(inicioSemanaAnterior(hoy));
        String texto = (String) r.get("texto");
        String corto = "TCD " + r.get("tcdPorcentaje") + "% · " + ((List<?>) r.get("sinTcd")).size()
                + " sin TCD · " + ((List<?>) r.get("faltaron")).size() + " ausencias. Toca para ver el detalle.";
        for (Persona admin : personaRepo.findActivasPorRol(Rol.ID_ADMIN)) {
            notificador.notificar(admin.getId(), "RESUMEN_SEMANAL", "📊 Resumen de la semana", corto);
            notificador.whatsapp(admin, texto, ConfiguracionServiceImpl.WA_RESUMEN_SEMANAL);
        }
        LOGGER.info("Resumen semanal {} enviado a los administradores", r.get("semanaInicio"));
    }

    public List<Map<String, Object>> ultimos() {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (ResumenSemanal rs : resumenRepo.findTop12ByOrderBySemanaInicioDesc()) {
            try {
                lista.add(mapper.readValue(rs.getContenido(), new TypeReference<Map<String, Object>>() { }));
            } catch (JsonProcessingException e) {
                LOGGER.warn("Resumen {} ilegible", rs.getId());
            }
        }
        return lista;
    }

    private void guardar(LocalDate lunes, LocalDate domingo, Map<String, Object> contenido) {
        try {
            ResumenSemanal rs = resumenRepo.findBySemanaInicio(Fechas.aDate(lunes)).orElseGet(ResumenSemanal::new);
            rs.setSemanaInicio(Fechas.aDate(lunes));
            rs.setSemanaFin(Fechas.aDate(domingo));
            rs.setContenido(mapper.writeValueAsString(contenido));
            rs.setFechaCreacion(new Date());
            resumenRepo.save(rs);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("No se pudo guardar el resumen semanal", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static String texto(Map<String, Object> r) {
        Map<String, Object> asis = (Map<String, Object>) r.get("asistencia");
        Map<String, Object> asisAnt = (Map<String, Object>) r.get("asistenciaAnterior");
        Map<String, Object> prox = (Map<String, Object>) r.get("proximaSemana");
        List<Map<String, Object>> sinTcd = (List<Map<String, Object>>) r.get("sinTcd");
        List<Map<String, Object>> faltaron = (List<Map<String, Object>>) r.get("faltaron");
        List<Map<String, Object>> cumple = (List<Map<String, Object>>) r.get("cumpleanos");
        List<Map<String, Object>> nuevos = (List<Map<String, Object>>) r.get("nuevos");

        StringBuilder sb = new StringBuilder();
        sb.append("📊 *Resumen semanal* (").append(r.get("semanaInicio")).append(" al ").append(r.get("semanaFin")).append(")\n\n");
        sb.append("🙏 *TCD:* ").append(r.get("tcdPorcentaje")).append("% ")
                .append(tendencia((Integer) r.get("tcdPorcentaje"), (Integer) r.get("tcdPorcentajeAnterior")))
                .append(" (").append(r.get("tcdParticipantes")).append(" participantes)\n");
        sb.append("✅ *Asistencia a turnos:* ").append(asis.get("porcentajeAsistencia")).append("% ")
                .append(tendencia((Integer) asis.get("porcentajeAsistencia"), (Integer) asisAnt.get("porcentajeAsistencia")))
                .append(" (").append(asis.get("turnos")).append(" turnos)\n");
        if (!sinTcd.isEmpty()) {
            sb.append("\n📖 *Sin TCD esta semana (").append(sinTcd.size()).append("):* ").append(nombres(sinTcd, 15)).append('\n');
        }
        if (!faltaron.isEmpty()) {
            sb.append("\n⚠️ *Faltaron o avisaron que no podían (").append(faltaron.size()).append("):* ")
                    .append(nombres(faltaron, 15)).append('\n');
        }
        sb.append("\n📅 *Próxima semana:* ").append(prox.get("turnos")).append(" turnos, ")
                .append(prox.get("pendientes")).append(" sin confirmar, ").append(prox.get("noPuede")).append(" no pueden\n");
        if (!cumple.isEmpty()) {
            sb.append("🎂 *Cumpleaños:* ");
            sb.append(cumple.stream().map(c -> c.get("nombre") + " (" + c.get("dia") + ")").collect(Collectors.joining(", ")));
            sb.append('\n');
        }
        if (!nuevos.isEmpty()) {
            sb.append("👋 *Nuevos registros:* ").append(nombres(nuevos, 15)).append('\n');
        }
        sb.append("\nDetalle: https://anunciaig.com/#/panel-pastor");
        return sb.toString();
    }

    private static String tendencia(Integer actual, Integer anterior) {
        if (actual == null || anterior == null) {
            return "";
        }
        int dif = actual - anterior;
        if (dif == 0) {
            return "(igual que la semana anterior)";
        }
        return dif > 0 ? "(▲ " + dif + " pts)" : "(▼ " + (-dif) + " pts)";
    }

    private static String nombres(List<Map<String, Object>> personas, int max) {
        String lista = personas.stream().limit(max).map(p -> String.valueOf(p.get("nombre")))
                .distinct().collect(Collectors.joining(", "));
        return personas.size() > max ? lista + " y " + (personas.size() - max) + " más" : lista;
    }
}
