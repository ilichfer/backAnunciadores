package com.anunciadores.service;

import com.anunciadores.model.Persona;
import com.anunciadores.model.RolPersona;
import com.anunciadores.model.Servicio;
import com.anunciadores.repository.IPersonaRepo;
import com.anunciadores.repository.IRolesPersonaRepo;
import com.anunciadores.repository.IServicioRepo;
import com.anunciadores.repository.ITdcRepo;
import com.anunciadores.util.Fechas;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Indicadores generales de la iglesia para el pastor: miembros, TCD, asistencia y crecimiento.
 */
@Service
public class PanelPastorService {

    private static final Locale ES = new Locale("es", "CO");
    /** Ventana para considerar a alguien "participante" del TCD. */
    static final int DIAS_PARTICIPACION_TCD = 30;

    @Autowired
    private IPersonaRepo personaRepo;
    @Autowired
    private IRolesPersonaRepo rolesPersonaRepo;
    @Autowired
    private ITdcRepo tdcRepo;
    @Autowired
    private IServicioRepo servicioRepo;

    public Map<String, Object> panel() {
        LocalDate hoy = Fechas.hoy();
        List<Persona> personas = personaRepo.findAll();
        List<Persona> activas = personas.stream().filter(p -> Boolean.TRUE.equals(p.getEstado())).collect(Collectors.toList());
        Map<Integer, Persona> porId = personas.stream().collect(Collectors.toMap(Persona::getId, p -> p, (a, b) -> a));

        Map<String, Object> r = new LinkedHashMap<>();
        r.put("generado", hoy.toString());
        r.put("miembros", miembros(personas, activas));
        r.put("crecimiento", crecimiento(personas, hoy));
        r.put("tcd", tcd(hoy, porId));
        r.put("asistencia", asistencia(hoy));
        r.put("cumpleanosProximos", cumpleanos(activas, hoy, 7));
        r.put("ausenciasProximas", ausenciasProximas(hoy, porId));
        return r;
    }

    // ── Miembros ─────────────────────────────────────────────────────────────

    private Map<String, Object> miembros(List<Persona> personas, List<Persona> activas) {
        Set<Integer> idsActivos = activas.stream().map(Persona::getId).collect(Collectors.toSet());
        Map<String, Integer> porRol = new LinkedHashMap<>();
        porRol.put("ADMIN", 0);
        porRol.put("SERVIDOR", 0);
        porRol.put("USUARIO", 0);
        Set<Integer> conRol = new HashSet<>();
        for (RolPersona rp : rolesPersonaRepo.findAll()) {
            if (!idsActivos.contains(rp.getIdPersona()) || !conRol.add(rp.getIdPersona())) {
                continue;
            }
            String rol = rp.getIdRol() == 1 ? "ADMIN" : rp.getIdRol() == 2 ? "SERVIDOR" : "USUARIO";
            porRol.merge(rol, 1, Integer::sum);
        }
        porRol.merge("USUARIO", idsActivos.size() - conRol.size(), Integer::sum);

        Map<String, Object> m = new LinkedHashMap<>();
        m.put("total", personas.size());
        m.put("activos", activas.size());
        m.put("inactivos", personas.size() - activas.size());
        m.put("porRol", porRol);
        return m;
    }

    // ── Crecimiento: personas nuevas por mes (ultimos 12 meses) ──────────────

    private Map<String, Object> crecimiento(List<Persona> personas, LocalDate hoy) {
        YearMonth actual = YearMonth.from(hoy);
        Map<YearMonth, Integer> nuevos = new LinkedHashMap<>();
        for (int i = 11; i >= 0; i--) {
            nuevos.put(actual.minusMonths(i), 0);
        }
        int sinFecha = 0;
        for (Persona p : personas) {
            LocalDate llegada = p.getFechaRegistro() != null
                    ? Fechas.aLocalDate(p.getFechaRegistro())
                    : Fechas.parsearTexto(p.getFechaLlegadaAdc());
            if (llegada == null) {
                sinFecha++;
                continue;
            }
            nuevos.computeIfPresent(YearMonth.from(llegada), (k, v) -> v + 1);
        }
        List<Map<String, Object>> serie = new ArrayList<>();
        for (Map.Entry<YearMonth, Integer> e : nuevos.entrySet()) {
            serie.add(punto(etiquetaMes(e.getKey()), e.getKey().toString(), e.getValue()));
        }
        Map<String, Object> c = new LinkedHashMap<>();
        c.put("nuevosPorMes", serie);
        c.put("nuevosEsteMes", nuevos.get(actual));
        c.put("nuevosMesAnterior", nuevos.get(actual.minusMonths(1)));
        c.put("personasSinFechaDeLlegada", sinFecha);
        return c;
    }

    // ── TCD ──────────────────────────────────────────────────────────────────

    private Map<String, Object> tcd(LocalDate hoy, Map<Integer, Persona> porId) {
        Set<Integer> participantes = participantesTcd(hoy);
        YearMonth mes = YearMonth.from(hoy);

        Map<String, Object> t = new LinkedHashMap<>();
        t.put("participantes", participantes.size());
        t.put("cumplimientoMes", porcentajeTcd(mes.atDay(1), hoy, participantes.size()));
        t.put("subieronHoy", personasConTcd(hoy, hoy).size());

        List<Map<String, Object>> porMes = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            YearMonth m = mes.minusMonths(i);
            LocalDate fin = i == 0 ? hoy : m.atEndOfMonth();
            int part = personasConTcd(m.atDay(1), fin).size();
            porMes.add(punto(etiquetaMes(m), m.toString(), porcentajeTcd(m.atDay(1), fin, part)));
        }
        t.put("cumplimientoPorMes", porMes);

        List<Map<String, Object>> porSemana = new ArrayList<>();
        LocalDate lunes = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        for (int i = 7; i >= 0; i--) {
            LocalDate inicio = lunes.minusWeeks(i);
            LocalDate fin = i == 0 ? hoy : inicio.plusDays(6);
            porSemana.add(punto(inicio.getDayOfMonth() + " " + inicio.getMonth().getDisplayName(TextStyle.SHORT, ES),
                    inicio.toString(), porcentajeTcd(inicio, fin, participantesTcd(fin).size())));
        }
        t.put("cumplimientoPorSemana", porSemana);

        Set<Integer> conTcdSemana = personasConTcd(hoy.minusDays(6), hoy);
        List<Map<String, Object>> faltan = participantes.stream()
                .filter(id -> !conTcdSemana.contains(id))
                .map(porId::get)
                .filter(p -> p != null && Boolean.TRUE.equals(p.getEstado()))
                .sorted(Comparator.comparing(Persona::getNombre, String.CASE_INSENSITIVE_ORDER))
                .map(PanelPastorService::personaCorta)
                .collect(Collectors.toList());
        t.put("sinTcdUltimos7Dias", faltan);
        return t;
    }

    /** Personas con al menos un TCD en los ultimos 30 dias: a ellas se les mide el cumplimiento. */
    public Set<Integer> participantesTcd(LocalDate hasta) {
        return personasConTcd(hasta.minusDays(DIAS_PARTICIPACION_TCD - 1), hasta);
    }

    public Set<Integer> personasConTcd(LocalDate desde, LocalDate hasta) {
        return new HashSet<>(tdcRepo.findPersonasConTcd(Fechas.aDate(desde), Fechas.aDate(hasta)));
    }

    /** % de dias con TCD: (dias-persona con TCD) / (participantes x dias del periodo). */
    int porcentajeTcd(LocalDate desde, LocalDate hasta, int participantes) {
        long dias = hasta.toEpochDay() - desde.toEpochDay() + 1;
        if (participantes == 0 || dias <= 0) {
            return 0;
        }
        int diasConTcd = tdcRepo.findDiasTcd(Fechas.aDate(desde), Fechas.aDate(hasta)).size();
        return (int) Math.min(100, Math.round(diasConTcd * 100.0 / (participantes * dias)));
    }

    // ── Asistencia a turnos ──────────────────────────────────────────────────

    private Map<String, Object> asistencia(LocalDate hoy) {
        YearMonth mes = YearMonth.from(hoy);
        Map<String, Object> a = new LinkedHashMap<>(conteoTurnos(mes.atDay(1), mes.atEndOfMonth()));
        List<Map<String, Object>> porMes = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            YearMonth m = mes.minusMonths(i);
            Map<String, Object> c = conteoTurnos(m.atDay(1), m.atEndOfMonth());
            porMes.add(punto(etiquetaMes(m), m.toString(), (Integer) c.get("porcentajeAsistencia")));
        }
        a.put("asistenciaPorMes", porMes);
        return a;
    }

    /** Turnos del periodo: asistencia registrada (PRESENTE/TARDE/AUSENTE) y confirmaciones. */
    Map<String, Object> conteoTurnos(LocalDate desde, LocalDate hasta) {
        List<Servicio> turnos = servicioRepo.findByRango(Fechas.aDate(desde), Fechas.aDate(hasta));
        int presentes = 0;
        int tarde = 0;
        int ausentes = 0;
        int confirmados = 0;
        int noPuede = 0;
        for (Servicio s : turnos) {
            String asis = s.getAsistencia() == null ? "" : s.getAsistencia().toUpperCase(Locale.ROOT);
            if ("PRESENTE".equals(asis) || "ASISTIO".equals(asis)) {
                presentes++;
            } else if ("TARDE".equals(asis)) {
                tarde++;
            } else if ("AUSENTE".equals(asis)) {
                ausentes++;
            }
            if ("CONFIRMADO".equals(s.getConfirmacion())) {
                confirmados++;
            } else if ("NO_PUEDE".equals(s.getConfirmacion())) {
                noPuede++;
            }
        }
        int registrados = presentes + tarde + ausentes;
        Map<String, Object> c = new LinkedHashMap<>();
        c.put("turnos", turnos.size());
        c.put("presentes", presentes);
        c.put("tarde", tarde);
        c.put("ausentes", ausentes);
        c.put("sinRegistro", turnos.size() - registrados);
        c.put("porcentajeAsistencia", registrados == 0 ? 0 : (int) Math.round((presentes + tarde) * 100.0 / registrados));
        c.put("confirmados", confirmados);
        c.put("noPuede", noPuede);
        c.put("pendientes", turnos.size() - confirmados - noPuede);
        return c;
    }

    // ── Cumpleanos y ausencias ──────────────────────────────────────────────

    List<Map<String, Object>> cumpleanos(List<Persona> activas, LocalDate desde, int dias) {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Persona p : activas) {
            LocalDate nacimiento = Fechas.parsearTexto(p.getFechanacimiento());
            if (nacimiento == null) {
                continue;
            }
            MonthDay md = MonthDay.from(nacimiento);
            for (int i = 0; i < dias; i++) {
                LocalDate d = desde.plusDays(i);
                if (MonthDay.from(d).equals(md) || (md.getMonthValue() == 2 && md.getDayOfMonth() == 29
                        && !d.isLeapYear() && d.getMonthValue() == 2 && d.getDayOfMonth() == 28)) {
                    Map<String, Object> c = personaCorta(p);
                    c.put("fecha", d.toString());
                    c.put("dia", Fechas.legible(d));
                    lista.add(c);
                    break;
                }
            }
        }
        lista.sort(Comparator.comparing(m -> (String) m.get("fecha")));
        return lista;
    }

    private List<Map<String, Object>> ausenciasProximas(LocalDate hoy, Map<Integer, Persona> porId) {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Servicio s : servicioRepo.findByRango(Fechas.aDate(hoy), Fechas.aDate(hoy.plusDays(14)))) {
            if (!"NO_PUEDE".equals(s.getConfirmacion())) {
                continue;
            }
            Persona p = porId.get(s.getIdPersona());
            Map<String, Object> a = p != null ? personaCorta(p) : new LinkedHashMap<>();
            a.put("idServicio", s.getId());
            a.put("fecha", Fechas.aLocalDate(s.getFechaServicio()).toString());
            a.put("motivo", s.getMotivoConfirmacion());
            lista.add(a);
        }
        return lista;
    }

    // ── Utilidades ───────────────────────────────────────────────────────────

    static Map<String, Object> personaCorta(Persona p) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", p.getId());
        String nombre = ((p.getNombre() == null ? "" : p.getNombre()) + " "
                + (p.getApellido() == null ? "" : p.getApellido())).trim().replaceAll("\\s+", " ");
        m.put("nombre", nombre);
        return m;
    }

    private static Map<String, Object> punto(String etiqueta, String clave, int valor) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("etiqueta", etiqueta);
        m.put("clave", clave);
        m.put("valor", valor);
        return m;
    }

    private static String etiquetaMes(YearMonth m) {
        String nombre = m.getMonth().getDisplayName(TextStyle.SHORT, ES).replace(".", "");
        return Character.toUpperCase(nombre.charAt(0)) + nombre.substring(1) + " " + String.valueOf(m.getYear()).substring(2);
    }

    Map<Integer, Persona> personasPorId() {
        Map<Integer, Persona> mapa = new HashMap<>();
        for (Persona p : personaRepo.findAll()) {
            mapa.put(p.getId(), p);
        }
        return mapa;
    }

    List<Persona> activas() {
        return personaRepo.findAll().stream().filter(p -> Boolean.TRUE.equals(p.getEstado())).collect(Collectors.toList());
    }

    List<Servicio> turnos(LocalDate desde, LocalDate hasta) {
        return servicioRepo.findByRango(Fechas.aDate(desde), Fechas.aDate(hasta));
    }
}
