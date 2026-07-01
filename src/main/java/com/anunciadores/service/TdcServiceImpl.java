  package  com.anunciadores.service;
  import com.anunciadores.dto.TdcDto;
  import com.anunciadores.dto.TdcReporteDto;
  import com.anunciadores.model.Persona;
  import com.anunciadores.model.Tdc;
  import com.anunciadores.repository.IPersonaRepo;
  import com.anunciadores.repository.ITdcRepo;
  import com.anunciadores.service.interfaces.ITdcService;
  import com.anunciadores.util.UtilDate;
  import java.awt.Graphics2D;
  import java.awt.image.BufferedImage;
  import java.io.IOException;
  import java.text.ParseException;
  import java.text.SimpleDateFormat;
  import java.time.LocalDate;
  import java.time.OffsetDateTime;
  import java.time.format.DateTimeFormatter;
  import java.util.ArrayList;
  import java.util.Date;
  import java.util.List;
  import java.util.Optional;
  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.scheduling.annotation.EnableScheduling;
  import org.springframework.scheduling.annotation.Scheduled;
  import org.springframework.stereotype.Service;
  import org.springframework.transaction.annotation.Transactional;
  @Configuration
  @EnableScheduling
  @Service
  public class TdcServiceImpl implements ITdcService {
private final Logger log = LoggerFactory.getLogger(com.anunciadores.service.TdcServiceImpl.class);
      @Autowired
      private ITdcRepo TdcRepository;
      @Autowired
      private IPersonaRepo personaRepository;
      @Autowired
      private UtilDate utilDate;
      @Transactional
      public Tdc save(Date fechaCreacion, Tdc tdc) {
          try {
              List<Tdc> tdcDto = this.TdcRepository.findAllByDateAndPersona(fechaCreacion, tdc.getIdPersona());
              if (tdcDto.isEmpty() || tdcDto.size() <= 0) {
                  Tdc tdc1 = (Tdc) this.TdcRepository.save(tdc);
              } else {
                  throw new RuntimeException();
              }
          } catch (Exception e) {
              e.printStackTrace();
              throw new RuntimeException();
          }
          return tdc;
      }
      public Tdc saveTcdImage(String urlCloudflare, Integer idPersona) {
          try {
              Tdc saveTcd = new Tdc();
              Date sqlDate = utilDate.cargarfechaActualBogotaDate();
              saveTcd.setFechaCreacion(sqlDate);
              saveTcd.setIdPersona(idPersona.intValue());
              saveTcd.setUrlImage(urlCloudflare);
              return (Tdc) this.TdcRepository.save(saveTcd);
          }
catch (Exception e) {
              e.printStackTrace();
              throw new RuntimeException(e);
          }
      }
      public Tdc getById(int id) {
          Optional<Tdc> tdsDto = this.TdcRepository.findById(Integer.valueOf(id));
          if (tdsDto.isPresent()) {
              return tdsDto.get();
          }
          return new Tdc();
      }
      public Tdc getTdcById(int id) {
          Tdc tcdDto = (Tdc) this.TdcRepository.getById(Integer.valueOf(1));
          return tcdDto;
      }
      public List<TdcDto> getAll() {
          List<TdcDto> listaDto = new ArrayList<>();
          List<Tdc> lisTdc = this.TdcRepository.findAll();
          lisTdc.forEach(tdc -> listaDto.add(mapTdcDto(tdc)));
          return listaDto;
      }
      public List<TdcDto> getTdcByFecha(Date fecha) {
          List<TdcDto> listaDto = new ArrayList<>();
          List<Tdc> lisTdc = this.TdcRepository.findAllByDate(fecha);
          lisTdc.forEach(tdc -> listaDto.add(mapTdcDto(tdc)));
          return listaDto;
      }
      public boolean getTdcByFechaAndPersona(Date fecha, int idPersona) {
          try {
              this.log.info(" [UPLOAD] fecha a buscar: " + fecha);
              this.log.info(" [UPLOAD] idPersona a buscar: " + idPersona);
              List<Tdc> cantidadTdc = this.TdcRepository.findAllByDateAndPersona(fecha, idPersona);
              this.log.info(" [UPLOAD] TCD cargados: " + cantidadTdc.size());
              if (cantidadTdc.size() <= 0) {
                  return true;
              }
          } catch (Exception e) {
              e.printStackTrace();
          }
          return false;
      }
      public List<TdcReporteDto> findAllBetweenDates(Date fechaStart, Date fechaEnd) throws ParseException {
          List<TdcReporteDto> listareporte = new ArrayList<>();
          List<Object> objects = this.TdcRepository.findAllBetweenDates(fechaStart, fechaEnd);
          for (int j = 0; j < objects.size(); j++) {
              Object[] object = (Object[]) objects.get(j);
              TdcReporteDto dto = new TdcReporteDto();
              dto.setNombre(object[0].toString());
              dto.setCantidadEntregados(Integer.parseInt(object[1].toString()));
              dto.setIdPersona(Integer.parseInt(object[2].toString()));
              dto.setPorcentajeCumplimiento(calcularPorcentajeCumplimiento(Integer.parseInt(object[1].toString())));
              listareporte.add(dto);
          }
          return listareporte;
      }
      public TdcReporteDto findAllBetweenDatesAndPerson(Integer idPersona) throws ParseException {
          TdcReporteDto dto = new TdcReporteDto();
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          String dateBog = this.utilDate.cargarFechaBogotaConParametro("yyyy-MM-dd");
          DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          LocalDate ld = LocalDate.parse(dateBog, dtf);
          int diaActual = ld.getDayOfMonth();
          int year = ld.getYear();
          int month = ld.getMonthValue();
          String fechainicial = "" + year + "-" + month + "-1";
          String fechaFinal = "" + year + "-" + month + "-" + diaActual;
          java.util.Date date1 = sdf.parse(fechainicial);
          java.util.Date date2 = sdf.parse(fechaFinal);
          Optional<Object> objects = this.TdcRepository.findAllBetweenDatesAndPerson(date1, date2, idPersona);
          Object[] object = (Object[]) objects.get();
          if (object[0] != null) {
              dto.setNombre(object[0].toString());
              dto.setCantidadEntregados(Integer.parseInt(object[1].toString()));
              dto.setIdPersona(Integer.parseInt(object[2].toString()));
              dto.setPorcentajeCumplimiento(calcularPorcentajeCumplimiento(Integer.parseInt(object[1].toString())));
          }
          return dto;
      }
      private Double calcularPorcentajeCumplimiento(int cantidadRegistros) throws ParseException {
          String dateBog = this.utilDate.cargarFechaBogotaConParametro("yyyy-MM-dd");
          DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          LocalDate ld = LocalDate.parse(dateBog, dtf);
          int diaDelMes = ld.getDayOfMonth();
          Double resp = (double) cantidadRegistros / diaDelMes * 100.00;
          return Math.round(resp * 100.0) / 100.0;
      }
      private TdcDto mapTdcDto(Tdc tdc) {
          TdcDto dto = new TdcDto();
          dto.setId(tdc.getId());
          dto.setTdc(tdc.getTdc());
          dto.setFechaCreacion(utilDate.convertDateToString(tdc.getFechaCreacion()));
          dto.setNombredocumento(tdc.getNombredocumento());
          dto.setUrlImage(tdc.getUrlImage());
          try {
              dto.setPersona(this.personaRepository.findById(Integer.valueOf(tdc.getIdPersona())).get());
          } catch (Exception e) {
              e.printStackTrace();
          }
          return dto;
      }
      public List<TdcDto> findAllBetweenDatesByPersona(Date fechaStart, Date fechaEnd, int idPersona) {
          List<TdcDto> listaDto = new ArrayList<>();
          List<Tdc> listaTdcPersona = this.TdcRepository.findAllBetweenDatesByPersona(fechaStart, fechaEnd, idPersona);
          listaTdcPersona.forEach(tdc -> listaDto.add(mapTdcDto(tdc)));
          return listaDto;
      }
      public List<TdcDto> findAlltcdByPersona(int idPersona) throws ParseException {
          List<TdcDto> listaDto = new ArrayList<>();
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          String dateBog = this.utilDate.cargarFechaBogotaConParametro("yyyy-MM-dd");
          DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          LocalDate ld = LocalDate.parse(dateBog, dtf);
          int monthDays = ld.lengthOfMonth();
          int year = ld.getYear();
          int month = ld.getMonthValue();
          String fechainicial = "" + year + "-" + year + "-1";
          String fechaFinal = "" + year + "-" + year + "-" + month;
          java.util.Date date1 = sdf.parse(fechainicial);
          java.util.Date date2 = sdf.parse(fechaFinal);
          List<Tdc> listaTdcPersona = this.TdcRepository.findAllBetweenDatesByPersona(date1, date2, idPersona);
          listaTdcPersona.forEach(tdc -> listaDto.add(mapTdcDto(tdc)));
          return listaDto;
      }
      public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
          BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, 1);
          Graphics2D graphics2D = resizedImage.createGraphics();
          graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
          graphics2D.dispose();
          return resizedImage;
      }
      @Scheduled(fixedRate = 21600000L)
      public void buscarTDCRepetidos() throws ParseException {
          java.util.Date fechaactual = this.utilDate.cargarfechaActualBogotaDate();
          List<Persona> listP = this.personaRepository.findUsuarios();
          for (Persona p : listP) {
              List<Tdc> tdcPersona = this.TdcRepository.findAllByDateAndPersona(fechaactual, p.getId().intValue());
              if (tdcPersona.size() > 1)
for (int i = 1; i < tdcPersona.size(); i++)
                  this.TdcRepository.delete(tdcPersona.get(i));
          }
      }
      @Override
      public List<Tdc> getTdcByFechaAndPersonaList(Date fecha, int idPersona) {
          try {
              return TdcRepository.findAllByDateAndPersona(fecha, idPersona);
          } catch (Exception e) {
              log.error("Error al obtener TDC por fecha y persona", e);
              return new ArrayList<>();
          }
      }
  }
