  package com.anunciadores.controller;
  import com.anunciadores.auth.dto.reporRequest;
  import com.anunciadores.auth.dto.updateServiceRequest;
  import com.anunciadores.dto.*;
import com.anunciadores.model.Coordinador;
import com.anunciadores.model.Curso;
import com.anunciadores.model.Inscripciones;
import com.anunciadores.model.Ministerio;
import com.anunciadores.model.NotasCurso;
import com.anunciadores.model.Persona;
import com.anunciadores.model.Servicio;
import com.anunciadores.model.Tdc;
import com.anunciadores.repository.*;
import com.anunciadores.service.UsuarioService;
import com.anunciadores.service.interfaces.*;
import com.anunciadores.util.UtilDate;
import com.anunciadores.model.Notificacion;
import com.anunciadores.model.Rol;
import com.anunciadores.dto.ImagenMensualDto;
import com.anunciadores.dto.RegisterPersonaRequestDto;
  import com.fasterxml.jackson.core.JsonProcessingException;
  import com.fasterxml.jackson.databind.JsonMappingException;
  import java.io.IOException;
  import java.text.ParseException;
  import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
  import java.util.List;
  import javax.servlet.http.HttpServletRequest;
  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.format.annotation.DateTimeFormat;
  import org.springframework.http.HttpStatus;
  import org.springframework.http.ResponseEntity;
  import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
  import org.springframework.web.multipart.MultipartFile;
  import org.springframework.web.server.ResponseStatusException;
  @CrossOrigin(origins = {"*"}, allowedHeaders = {"*"})
  @RestController
  @RequestMapping({"/api"})
  public class PersonaReactController {
private Logger LOGGER = LoggerFactory.getLogger(com.anunciadores.controller.PersonaReactController.class);
    @Autowired
    private IPersonaService personaService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ICursoService cursoService;
    @Autowired
    private ICursoRepo cursoRepository;
    @Autowired
    private IBibliaService bibliaService;
    @Autowired
    private IPersonaRepo personaRepoImpl;
    @Autowired
    private ConsolidacionRepoImpl consolidacionDao;
    @Autowired
    private IPagoService pagoService;
    @Autowired
    private IServicioService servicioService;
    @Autowired
    private IMInisteryService ministeryService;
    @Autowired
    private IRolesRepo rolesPersonaRepo;
    @Autowired
    private IPermisosRepo permisosMenuRepo;
    @Autowired
    private IParamMenuRepo paramMenuRepo;
    @Autowired
    private IRolesRepo rolesDao;
    @Autowired
    private UtilDate utilDate;
    @Autowired
    private IR2UploadService r2UploadService;
    @Autowired
    private ITdcService tdcService;
    @Autowired
    private INotificacionService notificacionService;
    @Autowired
    private IImagenMensualService imagenMensualService;
    @Autowired
    private IContactoService contactoService;
    @Autowired
    private InscripcionRepo inscripcionRepo;
    List<Persona> personasList;
    List<PersonaDto> personasListDto;
    @GetMapping({"/users"})
    public ResponseEntity<List<PersonaReactDto>> users() throws JsonMappingException, JsonProcessingException, ParseException {
ResponseEntity<List<PersonaReactDto>> rp = null;
List<PersonaReactDto> listResult = this.personaService.findAllUsers();
if (listResult.size() > 0) {
rp = new ResponseEntity(listResult, null, HttpStatus.ACCEPTED);
      } else {
rp = new ResponseEntity(listResult, null, HttpStatus.INTERNAL_SERVER_ERROR);
      } 
return rp;
    }
    @GetMapping({"/events"})
    public ResponseEntity<ProgramationDto> events() throws JsonMappingException, JsonProcessingException, ParseException {
ResponseEntity<ProgramationDto> rp = null;
ProgramationDto result = this.servicioService.findNextServices(this.utilDate.cargarfechaActualBogotaDate());
if (result != null && result.getMinistries() != null && result.getMinistries().size() > 0) {
rp = new ResponseEntity(result, null, HttpStatus.ACCEPTED);
      } else {
rp = new ResponseEntity(result, null, HttpStatus.INTERNAL_SERVER_ERROR);
      } 
return rp;
    }
    @GetMapping({"/findprog"})
    public ResponseEntity<ProgramationDto> findprog(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") String date) throws JsonMappingException, JsonProcessingException, ParseException {
ResponseEntity<ProgramationDto> rp = null;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
Date fechaD = sdf.parse(date);
ProgramationDto result = this.servicioService.findServices(fechaD);
if (result != null && result.getMinistries() != null && result.getMinistries().size() > 0) {
rp = new ResponseEntity(result, null, HttpStatus.ACCEPTED);
      } else {
rp = new ResponseEntity(result, null, HttpStatus.INTERNAL_SERVER_ERROR);
      } 
return rp;
    }
    @GetMapping({"/ministries"})
    public ResponseEntity<List<Ministry>> ministries() throws JsonMappingException, JsonProcessingException, ParseException {
ResponseEntity<List<Ministry>> rp = null;
List<Ministry> listServResult = this.ministeryService.getAllministriesWithPositions();
if (listServResult.size() > 0) {
rp = new ResponseEntity(listServResult, null, HttpStatus.ACCEPTED);
      } else {
rp = new ResponseEntity(listServResult, null, HttpStatus.INTERNAL_SERVER_ERROR);
      } 
return rp;
    }
    @GetMapping({"/ministries/{idMinistry}"})
    public ResponseEntity<Ministry> getMinistries(@PathVariable Integer idMinistry) throws JsonMappingException, JsonProcessingException, ParseException {
ResponseEntity<Ministry> rp = null;
Ministry minResult = this.ministeryService.getMinistryWithPositions(idMinistry);
if (minResult != null && minResult.getPositions() != null && minResult.getPositions().size() > 0) {
rp = new ResponseEntity(minResult, null, HttpStatus.ACCEPTED);
      } else {
rp = new ResponseEntity(minResult, null, HttpStatus.INTERNAL_SERVER_ERROR);
      } 
return rp;
    }
    @GetMapping({"/ministries/{id}/personas"})
    public ResponseEntity<?> getPersonasByMinisterio(@PathVariable Integer id) {
      try {
List<PersonaDto> personas = this.servicioService.findPersonaByidMnisterio(id.intValue());
return ResponseEntity.ok(personas);
} catch (Exception e) {
return ResponseEntity.status(500)
.body("Error al obtener personas del ministerio: " + e.getMessage());
      } 
    }
    @PostMapping({"/ministries/addposition"})
    public ResponseEntity<?> addPositionToMinistrie(@RequestBody PosicionDto posicionDto) {
      try {
this.servicioService.savePosicion(posicionDto); return ResponseEntity.noContent().build();
} catch (ResponseStatusException e) {
return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getReason());
      } 
    }
    @GetMapping({"/user"})
    public ResponseEntity<?> getUsuarioLogueado(HttpServletRequest request) {
      try {
String authHeader = request.getHeader("Authorization");
if (authHeader == null || !authHeader.startsWith("Bearer ")) {
return ResponseEntity.status(401).body("Token no proporcionado");
        }
String token = authHeader.substring(7);
UserResponseDto user = this.personaService.getUsuarioDesdeToken(token);
return ResponseEntity.ok(user);
      }
catch (Exception e) {
return ResponseEntity.status(401).body("Token inválido o expirado");
      } 
    }
    @GetMapping({"/schedule/persona/{id}"})
    public ResponseEntity<?> getProgramacionPersona(@PathVariable Integer id) {
      try {
List<ServicioResponseDto> listServ = this.servicioService.buscarProgramacionMes(id.intValue());
return ResponseEntity.ok(listServ);
} catch (Exception e) {
return ResponseEntity.status(500)
.body("Error al obtener programación: " + e.getMessage());
      } 
    }
    @DeleteMapping({"/ministeries/{idMinisterio}/personas/{idPersona}"})
    public ResponseEntity<?> deletePersonaFromMinisterie(@PathVariable int idMinisterio, @PathVariable int idPersona) {
ResponseEntity<List<PersonaDto>> rp = null;
this.personaService.eliminarPersonaMinisterio(idPersona, idMinisterio);
List<PersonaDto> personasList = this.servicioService.findPersonaByidMnisterio(idMinisterio);
if (personasList.size() > 0) {
rp = new ResponseEntity(personasList, null, HttpStatus.ACCEPTED);
      } else {
rp = new ResponseEntity(personasList, null, HttpStatus.INTERNAL_SERVER_ERROR);
      } 
return rp;
    }
    @PostMapping({"/ministeries/addperson"})
    public ResponseEntity<?> addPersonaToMinisterie(@RequestBody AddPersonaRequest request) {
      try {
this.ministeryService.agregarPersonasAMinisterio(request.getIdPersona().intValue(), request.getIdMinisterio().intValue());
return ResponseEntity.noContent().build();
} catch (ResponseStatusException e) {
return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getReason());
      } 
    }
    @PostMapping({"/updateprog"})
    public ResponseEntity<?> updateprog(@RequestBody updateServiceRequest request) {
      try {
System.out.println("entro ");
this.ministeryService.updateService(request);
return ResponseEntity.noContent().build();
} catch (ResponseStatusException e) {
return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getReason());
} catch (ParseException e) {
return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
      } 
    }
    @PostMapping({"/upload"})
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file, @RequestParam("idPersona") Integer idPersona) throws ParseException {
        ResponseEntity<String> rp = null;
        LOGGER.info("[UPLOAD] idPersona={} | fecha={} | archivo={} | tamaño={}KB | tipo={}",
                idPersona,
                utilDate.cargarfechaActualBogotaString(),
                file.getOriginalFilename(),
                file.getSize() / 1024,
                file.getContentType()
        );
        try {
            boolean tcd = tdcService.getTdcByFechaAndPersona(utilDate.cargarfechaActualBogotaDate(),idPersona);
            if (tcd ) {
                String imageUrl = this.r2UploadService.uploadImage(file);
ImagenDiariaDto imagen = new ImagenDiariaDto();
imagen.setIdPersona(idPersona.intValue());
imagen.setFechaCreacion(LocalDate.now());
imagen.setTdc(imageUrl);
this.tdcService.saveTcdImage(imageUrl, idPersona);
return ResponseEntity.ok(imageUrl);
            }
            rp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("tcd ya cargado para hoy "+ utilDate.cargarfechaActualBogotaString());
} catch (IOException e) {
            rp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (ParseException e) {
            rp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return rp;
    }
        @PostMapping({"/scheduleByDate"})
    public ResponseEntity<?> scheduleByDate(@RequestBody reporRequest request) throws ParseException {
ResponseEntity<List<TdcReporteDto>> rp = null;
if (request.getFechaInicio() != null && request.getFechaFin() != null) {
System.out.println("Fecha Inicio: " + request.getFechaInicio());
List<TdcReporteDto> listaTdc = this.tdcService.findAllBetweenDates(this.utilDate.convertStringToDate(request.getFechaInicio()), this.utilDate.convertStringToDate(request.getFechaFin()));
if (listaTdc.size() > 0) {
rp = new ResponseEntity(listaTdc, null, HttpStatus.ACCEPTED);
        } else {
rp = new ResponseEntity(listaTdc, null, HttpStatus.INTERNAL_SERVER_ERROR);
        } 
      } 
return rp;
    }
    @GetMapping({"/tdcbyPerson/{idPersona}"})
    public ResponseEntity<?> tdcbyPerson(@PathVariable Integer idPersona) throws ParseException {
ResponseEntity<TdcReporteDto> rp = null;
if (idPersona != null) {
TdcReporteDto Tdc = this.tdcService.findAllBetweenDatesAndPerson(idPersona);
if (Tdc != null && Tdc.getIdPersona() != 0) {
rp = new ResponseEntity(Tdc, null, HttpStatus.ACCEPTED);
        } else {
rp = new ResponseEntity(Tdc, null, HttpStatus.INTERNAL_SERVER_ERROR);
        } 
      } 
return rp;
    }
    @PostMapping({"/savecordinador"})
    public ResponseEntity<?> saveCordinador(@RequestBody CoordinadorDTO cordinador) {
      ResponseEntity<?> rp;
        Boolean response= Boolean.FALSE;
      try {
       Coordinador corSave = this.servicioService.findCoordinadorByFecha(this.utilDate.convertStringToDate(cordinador.getFechaString()));
       if (corSave == null) {
         response = this.servicioService.saveCoordinado(cordinador);
         if (response.booleanValue()) {
           rp = new ResponseEntity(Boolean.TRUE, null, HttpStatus.ACCEPTED);
          } else {
           rp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar cordinador");
          }
        } else {
            response = this.servicioService.updateCoordinador(corSave,cordinador.getIdPersona());}
          if (response.booleanValue()) {
              rp = new ResponseEntity(Boolean.TRUE, null, HttpStatus.ACCEPTED);
          } else {
              rp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar cordinador");
          }
     } catch (Exception e) {
       rp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar cordinador");
      } 
     return rp;
    }
    @GetMapping({"/findSchedule"})
    public ResponseEntity<?> consultarMiProgramacion(@RequestParam String fecha, @RequestParam int idMinisterio) throws JsonMappingException, JsonProcessingException, ParseException {
      ResponseEntity<?> rp;
Date fechaD = this.utilDate.convertStringToDate(fecha);
List<ServicioResponseDto> listProgramacionMinisterio = this.servicioService.findProgramacionByDateAndMinisterio(fechaD, idMinisterio);
if (listProgramacionMinisterio.size() > 0) {
rp = new ResponseEntity(listProgramacionMinisterio, null, HttpStatus.ACCEPTED);
      } else {
rp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar cordinador");
      } 
return rp;
    }
    @PostMapping({"/updatePassword"})
    public ResponseEntity<?> updatePass(@RequestBody Persona persona) throws JsonMappingException, JsonProcessingException {
ResponseEntity<?> rp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar contraseña");
PersonaDto per = this.personaService.buscarByDocumento(persona.getDocumento());
if (per.getEmail() != null) {
per.setPassword(persona.getPassword());
persona = this.personaService.personaDtoToEntity(per);
persona.setEstado(Boolean.valueOf(true));
Persona personaSave = this.personaService.savePassword(persona);
if (personaSave != null) {
rp = new ResponseEntity(personaSave, null, HttpStatus.ACCEPTED);
        } else {
rp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar contraseña");
        } 
      } 
return rp;
    }
    @PostMapping({"/saveInformCoordinator"})
    public ResponseEntity<?> saveInformCoordinator(@RequestBody CoordinadorDTO cordinador) throws ParseException {
      ResponseEntity<?> rp;
      try {
SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
Persona per = this.personaService.findPersonaById(cordinador.getPersona().getId());
cordinador.setPersona(per);
Boolean save = this.servicioService.saveCoordinadorEntity(cordinador);
if (save.booleanValue()) {
rp = ResponseEntity.status(HttpStatus.ACCEPTED).body("informe guardado exitosamente");
        } else {
rp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar informe del coordinador");
        } 
} catch (Exception e) {
rp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar informe del coordinador");
      } 
return rp;
    }
    @GetMapping({"/findBirthday"})
    public ResponseEntity<?> findBirthday() throws JsonMappingException, JsonProcessingException, ParseException {
      ResponseEntity<?> rp;
List<PersonaDto> listadoCumpleanosMes = this.personaService.findBirthdayByMonth();
List<PersonaDto> listadoCumpleanosDiario = this.personaService.getBirthDay(listadoCumpleanosMes);
if (listadoCumpleanosDiario.size() > 0) {
rp = new ResponseEntity(listadoCumpleanosDiario, null, HttpStatus.ACCEPTED);
      } else {
rp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("no hay cumpleaños hoy");
      } 
return rp;
    }
    @GetMapping({"/findBirthDaysMOnth"})
    public ResponseEntity<?> findBirthDaysMOnth() throws ParseException {
      ResponseEntity<?> rp;
List<PersonaDto> listadoCumpleanosMes = this.personaService.findBirthdayByMonth();
if (listadoCumpleanosMes.size() > 0) {
rp = new ResponseEntity(listadoCumpleanosMes, null, HttpStatus.ACCEPTED);
      } else {
rp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("no hay cumpleaños este mes");
      } 
return rp;
    }
    @GetMapping({"/personas/{idpersona}/toggle-active"})
    public ResponseEntity<?> toggleActive(@PathVariable Integer idpersona, @RequestParam boolean active) {
      ResponseEntity<?> rp;
Persona retorno = this.personaService.toggleActive(idpersona, Boolean.valueOf(active));
if (retorno.getId() != null) {
rp = new ResponseEntity(retorno, null, HttpStatus.ACCEPTED);
      } else {
rp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("so se puedo actualizar el estado del usuario");
      } 
return rp;
    }
    @PutMapping({"/personas/{idPersona}/rol"})
    public ResponseEntity<?> cambiarRol(@PathVariable Integer idPersona, @RequestParam Integer idRol) {
      try {
        if (idRol != Rol.ID_ADMIN && idRol != Rol.ID_USER) {
          return ResponseEntity.badRequest().body("idRol debe ser " + Rol.ID_ADMIN + " (ADMIN) o " + Rol.ID_USER + " (USER)");
        }
        personaService.findUsuariosRol(idPersona, idRol);
        String nombreRol = idRol == Rol.ID_ADMIN ? "ADMINISTRADOR" : "USER";
        return ResponseEntity.ok("Rol actualizado a " + nombreRol);
      } catch (Exception e) {
        LOGGER.error("Error al cambiar rol", e);
        return ResponseEntity.status(500).body("Error al cambiar rol: " + e.getMessage());
      }
    }
    @PostMapping({"/personas/register"})
    public ResponseEntity<?> registerPersona(@RequestBody RegisterPersonaRequestDto request) {
      try {
        Persona persona = new Persona();
        persona.setNombre(request.getNombre());
        persona.setApellido(request.getApellido());
        persona.setDocumento(request.getDocumento());
        persona.setTipodocumento(request.getTipodocumento());
        persona.setFechanacimiento(request.getFechanacimiento());
        persona.setGenero(request.getGenero());
        persona.setEstadoCivil(request.getEstadoCivil());
        persona.setEmail(request.getEmail());
        persona.setTelefono(request.getTelefono());
        persona.setCelular(request.getCelular());
        persona.setDireccion(request.getDireccion());
        persona.setCiudadDeptoDireccion(request.getCiudadDeptoDireccion());
        persona.setPaisNacimiento(request.getPaisNacimiento());
        persona.setCiudad(request.getCiudad());
        persona.setOcupacion(request.getOcupacion());
        persona.setEscolaridad(request.getEscolaridad());
        persona.setFechaConvercionCristo(request.getFechaConvercionCristo());
        persona.setFechaLlegadaAdc(request.getFechaLlegadaAdc());
        persona.setFechaBautizo(request.getFechaBautizo());
        persona.setFechaBautizoEspiritu(request.getFechaBautizoEspiritu());
        persona.setDiscapacidad(request.getDiscapacidad());
        persona.setDescDiscapacidad(request.getDescDiscapacidad());
        persona.setPerteneceMinoria(request.getPerteneceMinoria());
        persona.setDescMinoria(request.getDescMinoria());
        persona.setConsolidacion(request.getConsolidacion());
        persona.setPassword(request.getPassword());
        persona.setEstado(true);
        Persona saved = personaService.save(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
      } catch (Exception e) {
        LOGGER.error("Error al registrar persona", e);
        return ResponseEntity.status(500).body("Error al registrar persona: " + e.getMessage());
      }
    }
    @GetMapping({"/findTcdPerson"})
    public ResponseEntity<?> findTcdPerson(@RequestParam Integer idPersona, @RequestParam String fechaInicio, @RequestParam String fechaFin) throws ParseException {
      ResponseEntity<?> rp;
List<TdcDto> listTCD = this.tdcService.findAllBetweenDatesByPersona(this.utilDate.convertStringToDate(fechaInicio), this.utilDate.convertStringToDate(fechaFin), idPersona.intValue());
if (listTCD != null && listTCD.size() > 0) {
rp = new ResponseEntity(listTCD, null, HttpStatus.ACCEPTED);
      } else {
rp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("so se encontro tcd del usuario");
      } 
return rp;
    }
    @PostMapping({"/saveService"})
public ResponseEntity<?> saveService(@RequestBody List<ServiceDTO> request) {
      ResponseEntity<?> rp;
      try {
        List<Persona> listamultiple = this.servicioService.saveProgram(request);
        for (ServiceDTO dto : request) {
          if (dto.getIdPersona() != null && dto.getIdMinisterio() != null) {
            try {
              Integer idPersona = Integer.parseInt(dto.getIdPersona());
              Integer idMinisterio = Integer.parseInt(dto.getIdMinisterio());
              String posicion = dto.getIdPosicion() != null ? servicioService.findPosicion(Integer.valueOf(dto.getIdPosicion())).getNombrePosicion(): "Servicio";
              Ministerio min = this.servicioService.findByidMnisterio(idMinisterio);
              String nombreMinisterio = min != null ? min.getNombre() : "Ministerio";
              LocalDate fechaLocal = dto.getFechaServicio();
              Date fechaServicio = fechaLocal != null ? Date.from(fechaLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()) : new Date();
              this.notificacionService.crearNotificacionAsignacion(idPersona, fechaServicio, idMinisterio, nombreMinisterio, posicion);
            } catch (Exception ex) {
              LOGGER.warn("Error al crear notificación para servicio: ", ex);
            }
          }
        }
        rp = new ResponseEntity(listamultiple, null, HttpStatus.ACCEPTED);
      }
      catch (ResponseStatusException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getReason());
      }
      return rp;
    }
    @GetMapping({"/notificaciones/{idPersona}"})
    public ResponseEntity<?> getNotificaciones(@PathVariable Integer idPersona) {
      try {
        List<Notificacion> notificaciones = this.notificacionService.getNotificacionesPorPersona(idPersona);
        return ResponseEntity.ok(notificaciones);
      } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al obtener notificaciones: " + e.getMessage());
      }
    }
    @GetMapping({"/notificaciones/{idPersona}/no-leidas"})
    public ResponseEntity<?> getNotificacionesNoLeidas(@PathVariable Integer idPersona) {
      try {
        List<Notificacion> notificaciones = this.notificacionService.getNotificacionesNoLeidas(idPersona);
        return ResponseEntity.ok(notificaciones);
      } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al obtener notificaciones: " + e.getMessage());
      }
    }
    @PutMapping({"/notificaciones/{id}/leida"})
    public ResponseEntity<?> marcarLeida(@PathVariable Integer id) {
      try {
        Notificacion notificacion = this.notificacionService.marcarLeida(id);
        if (notificacion != null) {
          return ResponseEntity.ok(notificacion);
        }
        return ResponseEntity.status(404).body("Notificación no encontrada");
      } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al marcar notificación: " + e.getMessage());
      }
    }
    @PutMapping({"/notificaciones/{idPersona}/leer-todas"})
    public ResponseEntity<?> marcarTodasLeidas(@PathVariable Integer idPersona) {
      try {
        this.notificacionService.marcarTodasLeidas(idPersona);
        return ResponseEntity.ok(true);
      } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al marcar notificaciones: " + e.getMessage());
      }
    }
    @GetMapping({"/buscar/{id}"})
    public ResponseEntity<Object> getPersonaById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.personaService.findPersonaById(id));
    }
    @GetMapping({"/consutarEmail"})
    public ResponseEntity<PersonaDto> consutarEmail(@RequestParam String email) throws JsonMappingException, JsonProcessingException {
        PersonaDto person = this.personaService.buscarEmail(email);
        return new ResponseEntity(person, null, HttpStatus.ACCEPTED);
    }
    @GetMapping({"/notificaciones/{idPersona}/count"})
    public ResponseEntity<?> getCountNoLeidas(@PathVariable Integer idPersona) {
      try {
        Integer count = this.notificacionService.countNotificacionesNoLeidas(idPersona);
        return ResponseEntity.ok(count);
      } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al contar notificaciones: " + e.getMessage());
      }
    }
    @DeleteMapping({"/notificaciones/{idPersona}/limpiar/{dias}"})
    public ResponseEntity<?> limpiarNotificaciones(@PathVariable Integer idPersona, @PathVariable Integer dias) {
      try {
        this.notificacionService.eliminarNotificacionesAntiguas(idPersona, dias);
        return ResponseEntity.ok(true);
      } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al limpiar notificaciones: " + e.getMessage());
      }
    }
    @GetMapping({"/imagen-mensual/{tipo}"})
    public ResponseEntity<?> getImagenMensual(@PathVariable String tipo) {
      try {
        ImagenMensualDto dto = imagenMensualService.getImagenActual(tipo);
        if (dto == null) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay imagen disponible para: " + tipo);
        }
        return ResponseEntity.ok(dto);
      } catch (Exception e) {
        LOGGER.error("Error al obtener imagen mensual", e);
        return ResponseEntity.status(500).body("Error al obtener imagen mensual");
      }
    }
    @PostMapping({"/imagen-mensual/upload"})
    public ResponseEntity<?> uploadImagenMensual(
        @RequestParam("image") MultipartFile file,
        @RequestParam(value = "mes", required = false) Integer mes,
        @RequestParam(value = "anio", required = false) Integer anio,
        @RequestParam(value = "tipo", defaultValue = "tcd") String tipo) {
      try {
        if (file.isEmpty()) {
          return ResponseEntity.badRequest().body("El archivo es requerido");
        }
        String imageUrl = r2UploadService.uploadImage(file);
        if (imageUrl == null) {
          return ResponseEntity.status(500).body("Error al subir la imagen a Cloudflare R2");
        }
        if (mes == null) {
          mes = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1;
        }
        if (anio == null) {
          anio = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        }
        com.anunciadores.model.ImagenMensual guardada = imagenMensualService.guardarImagen(imageUrl, mes, anio, tipo);
        if (guardada == null) {
          return ResponseEntity.status(500).body("Error al guardar el registro en la base de datos");
        }
        LOGGER.info("Imagen mensual guardada: tipo={}, mes={}, anio={}, url={}", tipo, mes, anio, imageUrl);
        return ResponseEntity.ok(new ImagenMensualDto(guardada.getUrl(), guardada.getMes(), guardada.getAnio(), guardada.getTipo()));
      } catch (IOException e) {
        LOGGER.error("Error de IO al subir imagen mensual", e);
        return ResponseEntity.status(500).body("Error al procesar el archivo: " + e.getMessage());
      } catch (Exception e) {
        LOGGER.error("Error al guardar imagen mensual", e);
        return ResponseEntity.status(500).body("Error al guardar imagen mensual");
      }
    }
    @GetMapping({"/dashboard/stats/{idPersona}"})
    public ResponseEntity<?> getDashboardStats(@PathVariable Integer idPersona) {
      try {
        com.anunciadores.dto.DashboardStatsDto stats = new com.anunciadores.dto.DashboardStatsDto();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        Date fechaActual = utilDate.cargarfechaActualBogotaDate();
        cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
        cal.set(java.util.Calendar.MINUTE, 59);
        cal.set(java.util.Calendar.SECOND, 59);
        Date fechaLimite7dias = cal.getTime();
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        Date inicioMes = cal.getTime();
        cal.set(java.util.Calendar.DAY_OF_MONTH, cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
        cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
        cal.set(java.util.Calendar.MINUTE, 59);
        cal.set(java.util.Calendar.SECOND, 59);
        Date finMes = cal.getTime();
        List<Servicio> serviciosProximos = servicioService.getServiciosProximosPersona(idPersona, fechaActual, fechaLimite7dias);
        stats.setServiciosProximos(serviciosProximos.size());
        List<Servicio> serviciosMes = servicioService.getServiciosMesPersona(idPersona, inicioMes, finMes);
        stats.setServiciosDelMes(serviciosMes.size());
        int totalServiciosMes = serviciosMes.size();
        int serviciosAsistidos = 0;
        for (Servicio s : serviciosMes) {
          if ("ASISTIO".equalsIgnoreCase(s.getAsistencia())) {
            serviciosAsistidos++;
          }
        }
        stats.setTotalServiciosMes(totalServiciosMes);
        int porcentaje = totalServiciosMes > 0 ? (serviciosAsistidos * 100) / totalServiciosMes : 0;
        stats.setPorcentajeCumplimiento(porcentaje);
        Integer countNotif = notificacionService.countNotificacionesNoLeidas(idPersona);
        stats.setNotificacionesPendientes(countNotif != null ? countNotif : 0);
        List<com.anunciadores.model.Tdc> tdcHoy = tdcService.getTdcByFechaAndPersonaList(fechaActual, idPersona);
        stats.setTcdSubidoHoy(tdcHoy != null && !tdcHoy.isEmpty());
        List<com.anunciadores.dto.PersonaDto> cumpleaneros = personaService.findBirthdayByMonth();
        int proximosCumples = 0;
        java.util.Calendar calCumple = java.util.Calendar.getInstance();
        int diaHoy = calCumple.get(java.util.Calendar.DAY_OF_MONTH);
        int mesHoy = calCumple.get(java.util.Calendar.MONTH);
        for (com.anunciadores.dto.PersonaDto p : cumpleaneros) {
          if (p.getFechanacimiento() != null) {
            try {
              String[] partes = p.getFechanacimiento().split("/");
              if (partes.length >= 2) {
                int diaCumple = Integer.parseInt(partes[0]);
                if (diaCumple >= diaHoy && diaCumple <= diaHoy + 7) {
                  proximosCumples++;
                }
              }
            } catch (Exception ex) {}
          }
        }
        stats.setProximosCumpleanos(proximosCumples);
        List<com.anunciadores.dto.ProximoServicioDto> proximosServicios = new java.util.ArrayList<>();
        for (Servicio s : serviciosProximos) {
          if (s.getFechaServicio() != null) {
            String fechaStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(s.getFechaServicio());
            String nombreMinisterio = "";
            if (s.getIdMinisterio() > 0) {
              com.anunciadores.model.Ministerio min = servicioService.findByidMnisterio(s.getIdMinisterio());
              if (min != null) {
                nombreMinisterio = min.getNombre();
              }
            }
            proximosServicios.add(new com.anunciadores.dto.ProximoServicioDto(fechaStr, "", nombreMinisterio, ""));
          }
        }
        stats.setProximosServicios(proximosServicios);
        List<Object[]> serviciosPorMin = servicioService.getServiciosPorMinisterio(idPersona, inicioMes, finMes);
        List<com.anunciadores.dto.ServicioPorMinisterioDto> serviciosPorMinisterio = new java.util.ArrayList<>();
        for (Object[] row : serviciosPorMin) {
          String nombreMin = row[0] != null ? row[0].toString() : "";
          int cantidad = row[1] != null ? ((Number) row[1]).intValue() : 0;
          serviciosPorMinisterio.add(new com.anunciadores.dto.ServicioPorMinisterioDto(nombreMin, cantidad));
        }
        stats.setServiciosPorMinisterio(serviciosPorMinisterio);
        return ResponseEntity.ok(stats);
      } catch (Exception e) {
        LOGGER.error("Error al obtener stats del dashboard", e);
        return ResponseEntity.status(500).body("Error al obtener estadísticas: " + e.getMessage());
      }
    }
    @PostMapping({"/contacto"})
    public ResponseEntity<?> guardarContacto(@RequestBody ContactoRequestDto request) {
      try {
        ContactoDto resultado = contactoService.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
      } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
      } catch (Exception e) {
        LOGGER.error("Error al guardar contacto", e);
        return ResponseEntity.status(500).body("Error al procesar el mensaje");
      }
    }
    @GetMapping({"/contacto"})
    public ResponseEntity<?> listarContactos(
        @RequestParam(value = "soloNoLeidos", defaultValue = "false") boolean soloNoLeidos) {
      try {
        if (soloNoLeidos) {
          return ResponseEntity.ok(contactoService.listarNoLeidos());
        }
        return ResponseEntity.ok(contactoService.listarTodos());
      } catch (Exception e) {
        LOGGER.error("Error al listar contactos", e);
        return ResponseEntity.status(500).body("Error al obtener mensajes");
      }
    }
    @PutMapping({"/contacto/{id}/leer"})
    public ResponseEntity<?> marcarContactoLeido(@PathVariable Integer id) {
      try {
        ContactoDto resultado = contactoService.marcarLeido(id);
        return ResponseEntity.ok(resultado);
      } catch (IllegalArgumentException e) {
        return ResponseEntity.status(404).body(e.getMessage());
      } catch (Exception e) {
        LOGGER.error("Error al marcar contacto como leído", e);
        return ResponseEntity.status(500).body("Error al actualizar mensaje");
      }
    }
    @GetMapping({"/contacto/no-leidos/count"})
    public ResponseEntity<?> contarContactosNoLeidos() {
      try {
        long count = contactoService.contarNoLeidos();
        return ResponseEntity.ok(count);
      } catch (Exception e) {
        LOGGER.error("Error al contar contactos no leídos", e);
        return ResponseEntity.status(500).body(0L);
      }
    }
    @GetMapping({"/consutarDoc"})
    public ResponseEntity<PersonaDto> consutarDoc(@RequestParam int doc) throws JsonMappingException, JsonProcessingException, ParseException {
        PersonaDto person = this.personaService.buscarByDocumento(Integer.valueOf(doc));
        List<ServicioListResponseDto> listProgramacionMinisterio = this.servicioService.findProgramacionByDateGroup(this.utilDate.cargarfechaActualBogotaDate());
        if (listProgramacionMinisterio.size() > 0) {
            person.setCoordinadorActual(this.servicioService.validateCoordinadorByFechaAndIdPersona(listProgramacionMinisterio.get(0).getFechaServcio(), person.getId()).booleanValue());
        } else {
            person.setCoordinadorActual(false);
        }
        return new ResponseEntity(person, null, HttpStatus.ACCEPTED);
    }
    // ─── Endpoints de Cursos ────────────────────────────────────────────────────
    @GetMapping({"/cursos"})
    public ResponseEntity<?> listarCursos() {
      try {
        List<Curso> cursos = cursoService.findAll();
        return ResponseEntity.ok(cursos);
      } catch (Exception e) {
        LOGGER.error("Error al listar cursos", e);
        return ResponseEntity.status(500).body("Error al listar cursos: " + e.getMessage());
      }
    }
    @GetMapping({"/cursos/{id}"})
    public ResponseEntity<?> getCurso(@PathVariable int id) {
      try {
        Curso curso = cursoService.findCursoById(id);
        if (curso == null) {
          return ResponseEntity.status(404).body("Curso no encontrado");
        }
        return ResponseEntity.ok(curso);
      } catch (Exception e) {
        LOGGER.error("Error al obtener curso", e);
        return ResponseEntity.status(500).body("Error al obtener curso: " + e.getMessage());
      }
    }
    @PostMapping({"/cursos"})
    public ResponseEntity<?> crearCurso(@RequestBody CursoDto cursoDto) {
      try {
        Curso curso = cursoService.save(cursoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(curso);
      } catch (Exception e) {
        LOGGER.error("Error al crear curso", e);
        return ResponseEntity.status(500).body("Error al crear curso: " + e.getMessage());
      }
    }
    @PutMapping({"/cursos/{id}"})
    public ResponseEntity<?> actualizarCurso(@PathVariable int id, @RequestBody CursoDto cursoDto) {
      try {
        cursoDto.setId(id);
        Curso curso = cursoService.save(cursoDto);
        return ResponseEntity.ok(curso);
      } catch (Exception e) {
        LOGGER.error("Error al actualizar curso", e);
        return ResponseEntity.status(500).body("Error al actualizar curso: " + e.getMessage());
      }
    }
    @PutMapping({"/cursos/{id}/desactivar"})
    public ResponseEntity<?> desactivarCurso(@PathVariable int id) {
      try {
        Curso curso = cursoService.findCursoById(id);
        if (curso == null) {
          return ResponseEntity.status(404).body("Curso no encontrado");
        }
        cursoService.desactivarCurso(curso);
        return ResponseEntity.ok("Curso desactivado correctamente");
      } catch (Exception e) {
        LOGGER.error("Error al desactivar curso", e);
        return ResponseEntity.status(500).body("Error al desactivar curso: " + e.getMessage());
      }
    }
    @GetMapping({"/cursos/{id}/estudiantes"})
    public ResponseEntity<?> listarEstudiantesCurso(@PathVariable int id) {
      try {
        List<Inscripciones> inscripciones = inscripcionRepo.findByIdCurso(id);
        List<Persona> estudiantes = new java.util.ArrayList<>();
        for (Inscripciones ins : inscripciones) {
          personaRepoImpl.findById(ins.getIdPersona()).ifPresent(estudiantes::add);
        }
        return ResponseEntity.ok(estudiantes);
      } catch (Exception e) {
        LOGGER.error("Error al listar estudiantes del curso", e);
        return ResponseEntity.status(500).body("Error al listar estudiantes: " + e.getMessage());
      }
    }
    @PostMapping({"/cursos/{id}/estudiantes"})
    public ResponseEntity<?> inscribirEstudiante(@PathVariable int id, @RequestBody java.util.Map<String, Integer> body) {
      try {
        Integer idPersona = body.get("idPersona");
        if (idPersona == null) {
          return ResponseEntity.badRequest().body("idPersona es requerido");
        }
        java.util.Optional<Inscripciones> existente = inscripcionRepo.findByIdCursoAndIdPersona(id, idPersona);
        if (existente.isPresent()) {
          return ResponseEntity.status(HttpStatus.CONFLICT).body("El estudiante ya está inscrito en este curso");
        }
        Inscripciones inscripcion = new Inscripciones();
        inscripcion.setIdCurso(id);
        inscripcion.setIdPersona(idPersona);
        inscripcionRepo.save(inscripcion);
        return ResponseEntity.status(HttpStatus.CREATED).body("Estudiante inscrito correctamente");
      } catch (Exception e) {
        LOGGER.error("Error al inscribir estudiante", e);
        return ResponseEntity.status(500).body("Error al inscribir estudiante: " + e.getMessage());
      }
    }
    @DeleteMapping({"/cursos/{id}/estudiantes/{idPersona}"})
    public ResponseEntity<?> desinscribirEstudiante(@PathVariable int id, @PathVariable int idPersona) {
      try {
        inscripcionRepo.deleteByIdCursoAndIdPersona(id, idPersona);
        return ResponseEntity.ok("Estudiante desinscrito correctamente");
      } catch (Exception e) {
        LOGGER.error("Error al desinscribir estudiante", e);
        return ResponseEntity.status(500).body("Error al desinscribir estudiante: " + e.getMessage());
      }
    }
    @GetMapping({"/cursos/{id}/profesor"})
    public ResponseEntity<?> getProfesorCurso(@PathVariable int id) {
      try {
        Curso curso = cursoService.findCursoById(id);
        if (curso == null) {
          return ResponseEntity.status(404).body("Curso no encontrado");
        }
        Persona profesor = curso.getProfesor();
        if (profesor == null) {
          return ResponseEntity.status(404).body("No hay profesor asignado a este curso");
        }
        return ResponseEntity.ok(profesor);
      } catch (Exception e) {
        LOGGER.error("Error al obtener profesor del curso", e);
        return ResponseEntity.status(500).body("Error al obtener profesor: " + e.getMessage());
      }
    }
    @PutMapping({"/cursos/{id}/profesor"})
    public ResponseEntity<?> asignarProfesor(@PathVariable int id, @RequestBody java.util.Map<String, Integer> body) {
      try {
        Integer idPersona = body.get("idPersona");
        if (idPersona == null) {
          return ResponseEntity.badRequest().body("idPersona es requerido");
        }
        Curso curso = cursoService.findCursoById(id);
        if (curso == null) {
          return ResponseEntity.status(404).body("Curso no encontrado");
        }
        Persona profesor = personaRepoImpl.findById(idPersona).orElse(null);
        if (profesor == null) {
          return ResponseEntity.status(404).body("Persona no encontrada");
        }
        CursoDto cursoDto = new CursoDto();
        cursoDto.setId(curso.getId());
        cursoDto.setNombreCurso(curso.getNombreCurso());
        cursoDto.setFechaInicio(curso.getFechaInicio());
        cursoDto.setFechaFin(curso.getFechaFin());
        cursoDto.setValorTotal(curso.getValorTotal());
        cursoDto.setProfesor(idPersona);
        cursoService.save(cursoDto);
        return ResponseEntity.ok("Profesor asignado correctamente");
      } catch (Exception e) {
        LOGGER.error("Error al asignar profesor", e);
        return ResponseEntity.status(500).body("Error al asignar profesor: " + e.getMessage());
      }
    }
    // ─── Endpoints de Notas por Curso ──────────────────────────────────────────
    @GetMapping({"/cursos/{id}/notas"})
    public ResponseEntity<?> listarNotasCurso(@PathVariable int id) {
      try {
        List<Persona> estudiantes = new java.util.ArrayList<>();
        List<Inscripciones> inscripciones = inscripcionRepo.findByIdCurso(id);
        for (Inscripciones ins : inscripciones) {
          personaRepoImpl.findById(ins.getIdPersona()).ifPresent(estudiantes::add);
        }
        List<PersonaDto> resultado = cursoService.buscarNotasXPersonas(id, estudiantes);
        return ResponseEntity.ok(resultado);
      } catch (Exception e) {
        LOGGER.error("Error al listar notas del curso", e);
        return ResponseEntity.status(500).body("Error al listar notas: " + e.getMessage());
      }
    }
    @PostMapping({"/cursos/{id}/notas"})
    public ResponseEntity<?> guardarNota(@PathVariable int id, @RequestBody java.util.Map<String, Object> body) {
      try {
        int idPersona = ((Number) body.get("idPersona")).intValue();
        double notaMaestro = body.get("notaMaestro") != null ? ((Number) body.get("notaMaestro")).doubleValue() : 0;
        double notaAsistencia = body.get("notaAsistencia") != null ? ((Number) body.get("notaAsistencia")).doubleValue() : 0;
        double notaPractica = body.get("notaPractica") != null ? ((Number) body.get("notaPractica")).doubleValue() : 0;
        double notaExamenFinal = body.get("notaExamenFinal") != null ? ((Number) body.get("notaExamenFinal")).doubleValue() : 0;
        NotasCurso existente = cursoService.findNotasByCurso(id, idPersona);
        NotasCurso notas;
        if (existente != null) {
          notas = existente;
        } else {
          notas = new NotasCurso();
          notas.setId(id * 100000 + idPersona);
          Persona persona = personaRepoImpl.findById(idPersona).orElse(null);
          Curso curso = cursoService.findCursoById(id);
          notas.setCurso(curso);
          notas.setPersona(persona);
        }
        notas.setNotaMaestro(notaMaestro);
        notas.setNotaAsistencia(notaAsistencia);
        notas.setNotaPractica(notaPractica);
        notas.setNotaExamenFinal(notaExamenFinal);
        NotasCurso guardada = cursoService.saveNotasCurso(notas);
        return ResponseEntity.ok(guardada);
      } catch (Exception e) {
        LOGGER.error("Error al guardar nota", e);
        return ResponseEntity.status(500).body("Error al guardar nota: " + e.getMessage());
      }
    }
  }
