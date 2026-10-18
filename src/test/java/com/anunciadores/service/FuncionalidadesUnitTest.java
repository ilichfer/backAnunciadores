package com.anunciadores.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.anunciadores.repository.ITdcRepo;
import com.anunciadores.util.Fechas;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class FuncionalidadesUnitTest {

    @Test
    void normalizaCelularesColombianos() {
        assertEquals("573001234567", WhatsAppService.normalizarNumero("300 123 4567", "57"));
        assertEquals("573001234567", WhatsAppService.normalizarNumero("+57 300-123-4567", "57"));
        assertEquals("573001234567", WhatsAppService.normalizarNumero("573001234567", "57"));
        assertNull(WhatsAppService.normalizarNumero("6011234567", "57"));
        assertNull(WhatsAppService.normalizarNumero("Ali", "57"));
        assertNull(WhatsAppService.normalizarNumero(null, "57"));
    }

    @Test
    void rachaCuentaDiasSeguidosHastaHoyOAyer() {
        LocalDate hoy = Fechas.hoy();
        List<Date> dias = new ArrayList<>();
        // ayer, anteayer y hace 3 dias (hoy aun no sube): racha viva de 3
        for (int i = 1; i <= 3; i++) {
            dias.add(Fechas.aDate(hoy.minusDays(i)));
        }
        // racha antigua de 8 dias
        for (int i = 20; i < 28; i++) {
            dias.add(Fechas.aDate(hoy.minusDays(i)));
        }
        ITdcRepo repo = mock(ITdcRepo.class);
        when(repo.findFechasByPersona(5)).thenReturn(dias);
        TcdRachaService servicio = new TcdRachaService();
        ReflectionTestUtils.setField(servicio, "tdcRepo", repo);

        Map<String, Object> r = servicio.racha(5);
        assertEquals(3, r.get("rachaActual"));
        assertEquals(8, r.get("mejorRacha"));
        assertEquals(false, r.get("subidoHoy"));
        assertEquals(11, r.get("totalHistorico"));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> insignias = (List<Map<String, Object>>) r.get("insignias");
        assertTrue((Boolean) insignias.get(1).get("obtenida"), "Semana fiel con racha de 8");
        assertFalse((Boolean) insignias.get(2).get("obtenida"), "Habito de 21 aun no");
    }

    @Test
    void icsTieneFormatoValido() {
        CalendarioService servicio = new CalendarioService();
        Map<String, Object> e = new LinkedHashMap<>();
        e.put("idServicio", 99);
        e.put("fecha", "2026-10-25");
        e.put("posicion", "Guitarra, voz");
        e.put("ministerio", "Alabanza; jóvenes");
        e.put("persona", "Ana Paz");
        e.put("confirmacion", "NO_PUEDE");
        List<Map<String, Object>> eventos = new ArrayList<>();
        eventos.add(e);

        String ics = servicio.ics("Mis turnos", eventos);
        assertTrue(ics.startsWith("BEGIN:VCALENDAR\r\n"));
        assertTrue(ics.contains("DTSTART;VALUE=DATE:20261025"));
        assertTrue(ics.contains("DTEND;VALUE=DATE:20261026"));
        assertTrue(ics.contains("Guitarra\\, voz"), "las comas se escapan");
        assertTrue(ics.contains("Alabanza\\; jóvenes"), "los punto y coma se escapan");
        assertTrue(ics.contains("STATUS:CANCELLED"));
        for (String linea : ics.split("\r\n")) {
            assertTrue(linea.length() <= 75, "linea demasiado larga: " + linea);
        }
    }
}
