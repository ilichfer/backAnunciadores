  package  com.anunciadores.util;
  import java.text.ParseException;
  import java.text.SimpleDateFormat;
  import java.time.LocalDate;
  import java.time.ZoneId;
  import java.time.ZonedDateTime;
  import java.time.format.DateTimeFormatter;
  import java.util.Date;
  import org.springframework.stereotype.Service;
  @Service
  public class UtilDate
  {
    public String cargarfechaActualBogotaString() throws ParseException {
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
ZonedDateTime nowInBogota = ZonedDateTime.now(ZoneId.of("America/Bogota"));
String fechaActualStr = nowInBogota.format(formatter);
return fechaActualStr;
    }
      public Date cargarfechaActualBogotaDate() throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
ZonedDateTime nowInBogota = ZonedDateTime.now(ZoneId.of("America/Bogota"));
String fechaActualStr = nowInBogota.format(formatter);
          return convertStringToDate(fechaActualStr);
      }
    public String convertDateToString(Date fecha) {
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
String fechaString = sdf.format(fecha);
return fechaString;
    }
    public Date convertStringToDate(String fecha) throws ParseException {
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
Date fechaDate = sdf.parse(fecha);
return fechaDate;
    }
    public String convertDateToStringWithFormat(Date fecha, String format) throws ParseException {
SimpleDateFormat sdf = new SimpleDateFormat(format);
String fechaString = sdf.format(fecha);
return fechaString;
    }
    public String cargarFechaBogotaConParametro(String format) throws ParseException {
DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
ZonedDateTime nowInBogota = ZonedDateTime.now(ZoneId.of("America/Bogota"));
String fechaActualStr = nowInBogota.format(formatter);
return fechaActualStr;
    }
    public Date convertLocaldateToDate(LocalDate local) {
LocalDate localDate = local;
Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
System.out.println("LocalDate: " + localDate);
System.out.println("Date: " + date);
return date;
    }
    public Date[] getRangoDiaBogota(LocalDate dia) {
      ZoneId bogota = ZoneId.of("America/Bogota");
      Date inicio = Date.from(dia.atStartOfDay(bogota).toInstant());
      Date fin    = Date.from(dia.plusDays(1).atStartOfDay(bogota).toInstant());
      return new Date[]{inicio, fin};
    }
  }
