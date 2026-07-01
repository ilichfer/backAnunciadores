  package  com.anunciadores.service;
  import com.anunciadores.client.BibliaFeingClient;
  import com.anunciadores.dto.BibleFeingDto;
  import com.anunciadores.dto.BibliaDto;
  import com.anunciadores.dto.CapitulosDto;
  import com.anunciadores.dto.LibrosDto;
  import com.anunciadores.dto.VersiculoDto;
  import com.anunciadores.dto.VersiculoResponseDto;
  import com.anunciadores.dto.VersiculoSaveDto;
  import com.anunciadores.dto.VersiculosDto;
  import com.anunciadores.dto.VersionBiblesDto;
  import com.anunciadores.model.VersiculoSemanal;
  import com.anunciadores.repository.IVersiculoRepo;
  import com.anunciadores.service.interfaces.IBibliaService;
  import com.anunciadores.util.UtilDate;
  import com.fasterxml.jackson.core.JsonProcessingException;
  import com.fasterxml.jackson.databind.JsonMappingException;
  import com.fasterxml.jackson.databind.JsonNode;
  import com.fasterxml.jackson.databind.ObjectMapper;
  import com.fasterxml.jackson.databind.SerializationFeature;
  import java.text.ParseException;
  import java.text.SimpleDateFormat;
  import java.time.LocalDate;
  import java.time.format.DateTimeFormatter;
  import java.util.Collections;
  import java.util.Date;
  import java.util.Optional;
  import java.util.Random;
  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.beans.factory.annotation.Value;
  import org.springframework.http.HttpEntity;
  import org.springframework.http.HttpHeaders;
  import org.springframework.http.HttpMethod;
  import org.springframework.http.MediaType;
  import org.springframework.http.ResponseEntity;
  import org.springframework.stereotype.Service;
  import org.springframework.util.MultiValueMap;
  import org.springframework.web.client.RestTemplate;
  @Service
  public class BibliaServiceImpl
    implements IBibliaService
  {
    @Value("${token}")
    private String token;
    @Autowired
    BibliaFeingClient bibliaFeingClient;
private Logger LOGGER = LoggerFactory.getLogger(com.anunciadores.service.BibliaServiceImpl.class); @Autowired
    IVersiculoRepo versiculoRepo; @Autowired
    UtilDate utilDate;
    public BibliaDto findBible(String idioma) throws JsonMappingException, JsonProcessingException {
BibliaDto biblia = new BibliaDto();
StringBuilder request = new StringBuilder();
request.append("");
request.append("?language=" + idioma);
ResponseEntity<String> respuesta = consumirUrl(request.toString());
ObjectMapper mapper = new ObjectMapper();
mapper.enable(SerializationFeature.INDENT_OUTPUT);
JsonNode root = mapper.readTree((String)respuesta.getBody());
JsonNode data = root.path("data");
JsonNode bible = data.path(0);
JsonNode nombre = bible.path("name");
biblia.setName(nombre.asText());
JsonNode description = bible.path("description");
biblia.setDescription(description.asText());
JsonNode id = bible.path("id");
biblia.setId(id.asText());
return biblia;
    }
    public LibrosDto findBook(String idBible) throws JsonMappingException, JsonProcessingException {
LibrosDto librosDto = this.bibliaFeingClient.buscarLibro(idBible, this.token);
return librosDto;
    }
    public CapitulosDto findChapters(String idBible, String idBook) throws JsonMappingException, JsonProcessingException {
CapitulosDto capitulos = this.bibliaFeingClient.buscarCapitulos(idBible, idBook, this.token);
return capitulos;
    }
    public VersiculosDto findIdVerses(String idBible, String idChapter) throws JsonMappingException, JsonProcessingException {
VersiculosDto versiculos = this.bibliaFeingClient.buscarVersiculos(idBible, idChapter, this.token);
return versiculos;
    }
    public VersiculoResponseDto findVerse(String idBible, String idVerse) throws JsonMappingException, JsonProcessingException {
VersiculoResponseDto versiculo = this.bibliaFeingClient.buscarVersiculo(idBible, idVerse, this.token);
return versiculo;
    }
    private ResponseEntity<String> consumirUrl(String request) {
RestTemplate restTemplate = new RestTemplate();
HttpHeaders headers = new HttpHeaders();
headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
headers.set("api-key", this.token);
HttpEntity<String> entity = new HttpEntity("body", (MultiValueMap)headers);
return restTemplate.exchange(request, HttpMethod.GET, entity, String.class, new Object[0]);
    }
    public VersiculoDto findVerseDay() throws JsonMappingException, JsonProcessingException {
      try {
VersiculoDto pasajeError = new VersiculoDto();
pasajeError.setContent("[27] Yo soy el Señor, Dios de toda la humanidad. ¿Hay algo imposible para mí?");
pasajeError.setReference("Jeremías 32:27");
return pasajeError;
} catch (Exception e) {
System.out.println("error de servicio de biblia");
this.LOGGER.error("[findVerseDay] " + e.getMessage());
e.printStackTrace();
VersiculoDto pasajeError = new VersiculoDto();
pasajeError.setContent("[27] Yo soy el Señor, Dios de toda la humanidad. ¿Hay algo imposible para mí?");
pasajeError.setReference("Jeremías 32:27");
throw new RuntimeException("[findVerseDay]" + e.getMessage());
      } 
    }
    public LibrosDto findAllBooks() throws JsonMappingException, JsonProcessingException {
VersionBiblesDto versionesbiblia = this.bibliaFeingClient.buscarBiblia(this.token);
LibrosDto librosDto = findBook(((BibleFeingDto)versionesbiblia.getData().get(0)).getId());
return librosDto;
    }
    public VersiculoSaveDto saveVerseWeek(VersiculoSaveDto VersiculoSave) throws JsonProcessingException, ParseException {
VersiculoSemanal verSemanal = new VersiculoSemanal();
verSemanal.setCitaBiblica(VersiculoSave.getTitle());
verSemanal.setVersiculoTexto(VersiculoSave.getMessage());
verSemanal.setFechaInicio(this.utilDate.convertStringToDate(VersiculoSave.getFechaInicio()));
verSemanal.setFechaFin(this.utilDate.convertStringToDate(VersiculoSave.getFechaFin()));
Optional<VersiculoSemanal> verSave = Optional.of((VersiculoSemanal)this.versiculoRepo.save(verSemanal));
if (verSave.isPresent()) {
return VersiculoSave;
      }
throw new RuntimeException();
    }
    public VersiculoSaveDto buscarVersiculoSemanal() throws ParseException {
VersiculoSaveDto response = new VersiculoSaveDto();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String fechaActual = sdf.format(this.utilDate.cargarfechaActualBogotaDate());
DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
LocalDate ld = LocalDate.parse(fechaActual, dtf);
StringBuilder fechaIni = new StringBuilder();
StringBuilder fechaFin = new StringBuilder();
fechaIni.append("" + ld.getYear() + "-");
String mes = String.valueOf(ld.getMonthValue());
fechaIni.append((mes.length() < 2) ? ("0" + ld.getMonthValue() + "-") : ("" + ld.getMonthValue() + "-"));
String dia = String.valueOf(ld.getDayOfMonth());
fechaIni.append((dia.length() < 2) ? ("0" + ld.getDayOfMonth()) : Integer.valueOf(ld.getDayOfMonth()));
fechaFin.append("" + ld.getYear() + "-");
fechaFin.append((mes.length() < 2) ? ("0" + ld.getMonthValue() + "-") : ("" + ld.getMonthValue() + "-"));
String diafin = String.valueOf(ld.lengthOfMonth());
fechaFin.append((diafin.length() < 2) ? ("0" + ld.lengthOfMonth()) : Integer.valueOf(ld.lengthOfMonth()));
Date ini = this.utilDate.convertStringToDate(fechaIni.toString());
Date fin = this.utilDate.convertStringToDate(fechaFin.toString());
      try {
Optional<VersiculoSemanal> vSemanal = this.versiculoRepo.findByFechaFinBetween(ini, fin);
if (vSemanal.isPresent())
        {
response.setTitle(((VersiculoSemanal)vSemanal.get()).getCitaBiblica());
response.setMessage(((VersiculoSemanal)vSemanal.get()).getVersiculoTexto());
response.setFechaInicio(this.utilDate.convertDateToString(((VersiculoSemanal)vSemanal.get()).getFechaInicio()));
response.setFechaFin(this.utilDate.convertDateToString(((VersiculoSemanal)vSemanal.get()).getFechaFin()));
        }
} catch (Exception e) {
throw e;
      } 
return response;
    }
    public int libRamdom() {
int min = 1;
int max = 66;
Random random = new Random();
int value = random.nextInt(max + min) + min;
return value;
    }
    public int Ramdom(int max) {
int min = 1;
Random random = new Random();
int value = random.nextInt(max + min) + min;
return value;
    }
    public VersiculoDto constuirVersiculo() {
return null;
    }
  }
