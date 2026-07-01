  package  com.anunciadores.service;
  import com.anunciadores.client.TelegramClient;
  import com.anunciadores.dto.ContactDTO;
  import com.anunciadores.dto.MessageDTO;
  import com.anunciadores.dto.ResponseTelegram;
  import com.anunciadores.dto.ResultDTO;
  import com.anunciadores.dto.ServicioListResponseDto;
  import com.anunciadores.dto.ServicioResponseDto;
  import com.anunciadores.model.Coordinador;
  import com.anunciadores.model.Persona;
  import com.anunciadores.repository.IPersonaRepo;
  import com.anunciadores.service.interfaces.IServicioService;
  import com.anunciadores.service.interfaces.ITelegramService;
  import com.anunciadores.util.UtilDate;
  import com.fasterxml.jackson.core.JsonProcessingException;
  import com.fasterxml.jackson.databind.JsonMappingException;
  import java.sql.Date;
  import java.text.ParseException;
  import java.time.DayOfWeek;
  import java.time.LocalDate;
  import java.util.ArrayList;
  import java.util.HashMap;
  import java.util.HashSet;
  import java.util.List;
  import java.util.Map;
  import java.util.Optional;
  import java.util.Set;
  import java.util.stream.Collectors;
  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.beans.factory.annotation.Value;
  import org.springframework.scheduling.annotation.Scheduled;
  import org.springframework.stereotype.Service;
  @Service
  public class TelegramServiceImpl
    implements ITelegramService {
    @Autowired
    private IPersonaRepo personaRepo;
    @Autowired
    TelegramClient telegramClient;
private final Logger log = LoggerFactory.getLogger(com.anunciadores.service.TelegramServiceImpl.class); @Autowired
    private IServicioService servicioService; @Autowired
    private UtilDate utilDate; @Value("${propiedad.telegram.token}")
    private String telegramToken; public List<Persona> getUpdatesWithContact() throws JsonMappingException, JsonProcessingException {
List<Persona> peopleWithIdTelegram = new ArrayList<>();
ResponseTelegram response = this.telegramClient.getUpdates(this.telegramToken);
Set<Long> contactosUnicos = new HashSet<>();
List<ResultDTO> actualizacionesConContacto = (List<ResultDTO>)response.getResult().stream().filter(result -> { MessageDTO message = result.getMessage(); return (message != null && message.getContact() != null); }).filter(result -> contactosUnicos.add(result.getMessage().getChat().getId())).collect(Collectors.toList());
for (ResultDTO update : actualizacionesConContacto) {
ContactDTO contact = update.getMessage().getContact();
String telefonoOriginal = contact.getPhone_number();
String telefonoRecortado = (telefonoOriginal.length() > 2) ? telefonoOriginal.substring(2) : telefonoOriginal;
Long chatId = update.getMessage().getChat().getId();
String nombre = update.getMessage().getFrom().getFirst_name();
System.out.println("📞 Teléfono limpio: " + telefonoRecortado);
System.out.println("👤 Nombre: " + nombre);
System.out.println("💬 Chat ID: " + chatId);
System.out.println("────────────────────────────");
Optional<List<Persona>> personaTelegram = this.personaRepo.findByCelular(telefonoRecortado);
if (personaTelegram.isPresent() && ((List)personaTelegram.get()).size() > 1) {
for (Persona p : personaTelegram.get()) {
System.out.println("   - Persona encontrada: ID=" + p.getId() + ", Nombre=" + p.getNombre() + ", Celular=" + p.getCelular() + ", TelegramID=" + p.getIdTelegram());
p.setIdTelegram(chatId.toString());
peopleWithIdTelegram.add((Persona)this.personaRepo.save(p));
          }  continue;
}  if (personaTelegram.isPresent() && ((List)personaTelegram.get()).size() == 1) {
((Persona)((List<Persona>)personaTelegram.get()).get(0)).setIdTelegram(chatId.toString());
peopleWithIdTelegram.add((Persona)this.personaRepo.save(((List<Persona>)personaTelegram.get()).get(0))); continue;
        } 
System.out.println("❌ No existe");
      } 
return peopleWithIdTelegram;
    }
    public List<Persona> getContactTelegram() throws JsonMappingException, JsonProcessingException {
return (List<Persona>)this.personaRepo.findAll().stream().filter(p -> (p.getEstado().booleanValue() == true))
.collect(Collectors.toList());
    }
    public int getUpdatesWithOutContact() throws JsonMappingException, JsonProcessingException {
ResponseTelegram response = this.telegramClient.getUpdates(this.telegramToken);
Set<Long> contactosUnicos = new HashSet<>();
List<ResultDTO> actualizacionesSinDuplicados = (List<ResultDTO>)response.getResult().stream().filter(result -> { MessageDTO message = result.getMessage(); return (message != null && message.getContact() == null); }).filter(result -> contactosUnicos.add(result.getMessage().getChat().getId())).collect(Collectors.toList());
int contador = 0;
for (ResultDTO update : actualizacionesSinDuplicados) {
Long idChatTelegram = update.getMessage().getChat().getId();
Optional<List<Persona>> list = this.personaRepo.findByIdTelegram(idChatTelegram.toString());
if (((List)list.get()).size() > 0) {
System.out.println("❌ Ya tiene telegram asociado");
          continue;
        } 
getContact(idChatTelegram.toString());
System.out.println("✅ No tiene telegram asociado");
contador++;
      } 
return contador;
    }
    public List<ServicioResponseDto> sendNotification() throws JsonMappingException, JsonProcessingException, ParseException {
List<ServicioListResponseDto> listProgramacionMinisterio = this.servicioService.findProgramacionByDateGroup(this.utilDate.cargarfechaActualBogotaDate());
List<ServicioResponseDto> personasProgramandas = new ArrayList<>();
if (listProgramacionMinisterio.size() > 0) {
for (ServicioListResponseDto serv : listProgramacionMinisterio) {
for (ServicioResponseDto s : serv.getServicioDTO()) {
if (s.getIdNotificacionTelegram() != null) {
personasProgramandas.add(s);
Map<String, Object> body = new HashMap<>();
body.put("chat_id", Long.valueOf(Long.parseLong(s.getIdNotificacionTelegram())));
body.put("text", "Hola <b>" + s.getEncargado() + "</b>,\neste es un mensaje automático para recordarte que tienes servicio el día <b>" + serv.getFechaServcio() + "</b> en el ministerio de <b>" + serv.getNombreMinisterio() + "</b> en la posición <b>" + s.getPosicion() + "</b>.\n¡Gracias por tu servicio!");
body.put("disable_notification", Boolean.valueOf(false));
String respuesta = this.telegramClient.enviarMensajeTelegram(this.telegramToken, body, "HTML");
System.out.println("📨 Telegram respondió: " + respuesta); continue;
            } 
personasProgramandas.add(s);
System.out.println(s.getEncargado() + "   ❌ No tiene telegram asociado");
          } 
        } 
Coordinador cor = this.servicioService.findCoordinador(listProgramacionMinisterio);
if (cor != null && cor.getPersona() != null && cor.getPersona().getIdTelegram() != null) {
Map<String, Object> body = new HashMap<>();
body.put("chat_id", Long.valueOf(Long.parseLong(cor.getPersona().getIdTelegram())));
body.put("text", "Hola <b>" + cor.getPersona().getNombre() + "</b>,\neste es un mensaje automático para recordarte que tienes servicio el día <b>" + this.utilDate.convertDateToString(cor.getFechaServicio()) + "</b> en la posición de <b> Coordinador </b>.\n¡Gracias por tu servicio!");
body.put("disable_notification", Boolean.valueOf(false));
String respuesta = this.telegramClient.enviarMensajeTelegram(this.telegramToken, body, "HTML");
System.out.println("📨 Telegram respondió: " + respuesta);
        } 
      } 
return personasProgramandas;
    }
    public ResponseTelegram getContact(String idTelegram) throws JsonMappingException, JsonProcessingException {
if (!this.personaRepo.findFirstByIdTelegram(idTelegram).isPresent() && !idTelegram.equals("7726706876")) {
Map<String, Object> body = new HashMap<>();
body.put("chat_id", idTelegram);
body.put("text", "Hola, Dios te bendiga.\nnos encantaría poder estar en contacto contigo para compartir información y apoyarte en lo que necesites.\n👇 ¿Podrías compartirnos tu contacto telefonico dando click en el boton de abajo? 👇 ");
body.put("disable_notification", Boolean.valueOf(false));
Map<String, Object> botonContacto = new HashMap<>();
botonContacto.put("text", "Click aqui para compartir tu telefono");
botonContacto.put("request_contact", Boolean.valueOf(true));
List<Map<String, Object>> filaBotones = new ArrayList<>();
filaBotones.add(botonContacto);
List<List<Map<String, Object>>> teclado = new ArrayList<>();
teclado.add(filaBotones);
Map<String, Object> replyMarkup = new HashMap<>();
replyMarkup.put("keyboard", teclado);
replyMarkup.put("resize_keyboard", Boolean.valueOf(true));
replyMarkup.put("one_time_keyboard", Boolean.valueOf(true));
body.put("reply_markup", replyMarkup);
String respuesta = this.telegramClient.enviarMensajeTelegram(this.telegramToken, body, "HTML");
System.out.println("📨 Telegram respondió: " + respuesta);
      } 
return null;
    }
    @Scheduled(cron = "0 0 18 * * *", zone = "America/Bogota")
    public ResponseTelegram sendMessage() throws JsonMappingException, JsonProcessingException, ParseException {
LocalDate fechaActual = LocalDate.now();
DayOfWeek diaDeLaSemana = fechaActual.getDayOfWeek();
System.out.println("El día actual es: " + diaDeLaSemana);
if (diaDeLaSemana == DayOfWeek.THURSDAY || diaDeLaSemana == DayOfWeek.SATURDAY) {
fechaActual = fechaActual.plusDays(1L);
Date fechaAumentada = Date.valueOf(fechaActual);
List<ServicioListResponseDto> listProgramacionMinisterio = this.servicioService.findProgramacionByDateGroup(fechaAumentada);
if (listProgramacionMinisterio.size() > 0) {
for (ServicioListResponseDto serv : listProgramacionMinisterio) {
for (ServicioResponseDto s : serv.getServicioDTO()) {
if (s.getIdNotificacionTelegram() != null) {
Map<String, Object> body = new HashMap<>();
body.put("chat_id", Long.valueOf(Long.parseLong(s.getIdNotificacionTelegram())));
body.put("text", "Hola <b>" + s.getEncargado() + "</b>,\n este es un mensaje automático para recordarte que tienes servicio el día <b>" + serv.getFechaServcio() + "</b> en el ministerio de <b>" + serv.getNombreMinisterio() + "</b> en la posición <b>" + s.getPosicion() + "</b>. \n ¡Gracias por tu servicio!");
body.put("disable_notification", Boolean.valueOf(false));
String respuesta = this.telegramClient.enviarMensajeTelegram(this.telegramToken, body, "HTML");
System.out.println("📨 Telegram respondió: " + respuesta); continue;
              } 
System.out.println(s.getEncargado() + "   ❌ No tiene telegram asociado");
            } 
          } 
Coordinador cor = this.servicioService.findCoordinador(listProgramacionMinisterio);
if (cor != null && cor.getPersona() != null && cor.getPersona().getIdTelegram() != null) {
Map<String, Object> body = new HashMap<>();
body.put("chat_id", Long.valueOf(Long.parseLong(cor.getPersona().getIdTelegram())));
body.put("text", "Hola <b>" + cor.getPersona().getNombre() + "</b>,\n este es un mensaje automático para recordarte que tienes servicio el día <b>" + this.utilDate.convertDateToString(cor.getFechaServicio()) + "</b> en la posición de <b> Coordinador </b>. \n¡Gracias por tu servicio!");
body.put("disable_notification", Boolean.valueOf(false));
String respuesta = this.telegramClient.enviarMensajeTelegram(this.telegramToken, body, "HTML");
System.out.println("📨 Telegram respondió: " + respuesta);
          } 
        } 
      } else {
System.out.println("Hoy no ejecutar ña tarea.");
      } 
return null;
    }
  }
