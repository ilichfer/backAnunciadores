package com.anunciadores.service;

import com.anunciadores.repository.ITdcRepo;
import com.anunciadores.util.Fechas;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Racha de dias seguidos con TCD e insignias para motivar la constancia.
 */
@Service
public class TcdRachaService {

    @Autowired
    private ITdcRepo tdcRepo;

    public Map<String, Object> racha(int idPersona) {
        TreeSet<LocalDate> dias = new TreeSet<>();
        for (Date d : tdcRepo.findFechasByPersona(idPersona)) {
            LocalDate dia = Fechas.aLocalDate(d);
            if (dia != null) {
                dias.add(dia);
            }
        }
        LocalDate hoy = Fechas.hoy();

        // Racha actual: sigue viva si subio hoy o ayer
        int rachaActual = 0;
        LocalDate cursor = dias.contains(hoy) ? hoy : hoy.minusDays(1);
        while (dias.contains(cursor)) {
            rachaActual++;
            cursor = cursor.minusDays(1);
        }

        int mejorRacha = 0;
        int actual = 0;
        LocalDate previo = null;
        for (LocalDate d : dias) {
            actual = previo != null && previo.plusDays(1).equals(d) ? actual + 1 : 1;
            mejorRacha = Math.max(mejorRacha, actual);
            previo = d;
        }

        YearMonth mesActual = YearMonth.from(hoy);
        long totalMes = dias.stream().filter(d -> YearMonth.from(d).equals(mesActual)).count();
        boolean mesCompleto = tieneMesCompleto(dias, hoy);

        List<Map<String, Object>> insignias = new ArrayList<>();
        insignias.add(insignia("PRIMER_PASO", "Primer paso", "Subiste tu primer TCD", "fa-seedling",
                dias.size() >= 1, Math.min(dias.size(), 1), 1));
        insignias.add(insignia("SEMANA_FIEL", "Semana fiel", "7 días seguidos con Dios", "fa-calendar-week",
                mejorRacha >= 7, Math.min(mejorRacha, 7), 7));
        insignias.add(insignia("HABITO", "Hábito formado", "21 días seguidos", "fa-fire",
                mejorRacha >= 21, Math.min(mejorRacha, 21), 21));
        insignias.add(insignia("CUARENTA", "40 días con Dios", "40 días seguidos, como Jesús en el desierto",
                "fa-mountain-sun", mejorRacha >= 40, Math.min(mejorRacha, 40), 40));
        insignias.add(insignia("MES_COMPLETO", "Mes completo", "Todos los días de un mes", "fa-calendar-check",
                mesCompleto, mesCompleto ? 1 : 0, 1));
        insignias.add(insignia("CIEN", "100 encuentros", "100 TCD en total", "fa-crown",
                dias.size() >= 100, Math.min(dias.size(), 100), 100));

        Map<String, Object> r = new LinkedHashMap<>();
        r.put("rachaActual", rachaActual);
        r.put("mejorRacha", mejorRacha);
        r.put("subidoHoy", dias.contains(hoy));
        r.put("totalMes", totalMes);
        r.put("diasMesTranscurridos", hoy.getDayOfMonth());
        r.put("totalHistorico", dias.size());
        r.put("insignias", insignias);
        return r;
    }

    /** true si hay algun mes (ya terminado, o el actual si ya acabo) con TCD todos los dias. */
    private static boolean tieneMesCompleto(TreeSet<LocalDate> dias, LocalDate hoy) {
        Map<YearMonth, Integer> porMes = new LinkedHashMap<>();
        for (LocalDate d : dias) {
            porMes.merge(YearMonth.from(d), 1, Integer::sum);
        }
        for (Map.Entry<YearMonth, Integer> e : porMes.entrySet()) {
            YearMonth mes = e.getKey();
            boolean terminado = mes.isBefore(YearMonth.from(hoy)) || hoy.equals(mes.atEndOfMonth());
            if (terminado && e.getValue() >= mes.lengthOfMonth()) {
                return true;
            }
        }
        return false;
    }

    private static Map<String, Object> insignia(String codigo, String nombre, String descripcion, String icono,
                                                boolean obtenida, long progreso, long meta) {
        Map<String, Object> i = new LinkedHashMap<>();
        i.put("codigo", codigo);
        i.put("nombre", nombre);
        i.put("descripcion", descripcion);
        i.put("icono", icono);
        i.put("obtenida", obtenida);
        i.put("progreso", progreso);
        i.put("meta", meta);
        return i;
    }
}
