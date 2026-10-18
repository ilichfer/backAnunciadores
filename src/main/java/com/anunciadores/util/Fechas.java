package com.anunciadores.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Conversiones de fecha usadas por las funcionalidades nuevas.
 *
 * Las fechas de servicio y TCD se guardan como "dia de Bogota a medianoche en la zona del servidor",
 * igual que lo hace UtilDate, por eso las conversiones usan ZoneId.systemDefault().
 */
public final class Fechas {

    public static final ZoneId BOGOTA = ZoneId.of("America/Bogota");
    public static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final Locale ES = new Locale("es", "CO");
    private static final DateTimeFormatter LEGIBLE = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", ES);

    private Fechas() {
    }

    public static LocalDate hoy() {
        return LocalDate.now(BOGOTA);
    }

    public static Date aDate(LocalDate fecha) {
        return Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate aLocalDate(Date fecha) {
        if (fecha == null) {
            return null;
        }
        if (fecha instanceof java.sql.Date) {
            return ((java.sql.Date) fecha).toLocalDate();
        }
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /** "sábado 18 de octubre" */
    public static String legible(LocalDate fecha) {
        return fecha == null ? "" : fecha.format(LEGIBLE);
    }

    /** Interpreta fechas guardadas como texto (yyyy-MM-dd o dd/MM/yyyy). Devuelve null si no se puede. */
    public static LocalDate parsearTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        String t = texto.trim();
        try {
            if (t.length() >= 10 && t.charAt(4) == '-') {
                return LocalDate.parse(t.substring(0, 10), ISO);
            }
            if (t.length() >= 10 && t.charAt(2) == '/') {
                return LocalDate.parse(t.substring(0, 10), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
        } catch (DateTimeParseException e) {
            return null;
        }
        return null;
    }
}
