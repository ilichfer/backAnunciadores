  package  com.anunciadores.service;
  import com.anunciadores.dto.AsignacionServicioDTO;
  import com.anunciadores.dto.PersonaDto;
  import com.anunciadores.dto.PersonaReactDto;
  import com.anunciadores.dto.ServicioResponseDto;
  import com.anunciadores.dto.UserResponseDto;
  import com.anunciadores.mapper.mapperMensaje;
  import com.anunciadores.model.Consolidacion;
  import com.anunciadores.model.Curso;
  import com.anunciadores.model.EstudioPersona;
  import com.anunciadores.model.InscripcionActividad;
  import com.anunciadores.model.Inscripciones;
  import com.anunciadores.model.Pago;
  import com.anunciadores.model.ParamMenu;
  import com.anunciadores.model.PermisosMenu;
  import com.anunciadores.model.Persona;
  import com.anunciadores.model.Rol;
  import com.anunciadores.model.RolPersona;
  import com.anunciadores.model.Servicio;
  import com.anunciadores.model.inscripcionConsolidacion;
  import com.anunciadores.repository.*;
  import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
  import java.time.LocalDate;
  import java.time.ZoneId;
  import java.time.ZonedDateTime;
  import java.time.format.DateTimeFormatter;
  import java.util.ArrayList;
  import java.util.Date;
  import java.util.List;
  import java.util.Optional;
  import com.anunciadores.service.interfaces.ICursoService;
  import com.anunciadores.service.interfaces.IMenuService;
  import com.anunciadores.service.interfaces.IPagoService;
  import com.anunciadores.service.interfaces.IPersonaService;
  import com.anunciadores.util.UtilDate;
  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.data.domain.Sort;
  import org.springframework.jdbc.core.JdbcTemplate;
  import org.springframework.stereotype.Service;
  import org.springframework.transaction.annotation.Transactional;
  @Service
  @Transactional
  public class PersonaServiceImpl implements IPersonaService {
private Logger LOGGER = LoggerFactory.getLogger(com.anunciadores.service.PersonaServiceImpl.class);
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private IPersonaRepo personaRepository;
    @Autowired
    private IConsolidacionRepo iConsolidacionRepo;
    @Autowired
    private IRolesRepo rolesDao;
    @Autowired
    private IRolesRepo rolesRepo;
    @Autowired
    private IPermisosRepo permisosRepo;
    @Autowired
    private IMensajesRepo mensajesRepo;
    @Autowired
    private IServicioRepo servicioRepo;
    @Autowired
    private IMinisterioRepo ministerioRepo;
    @Autowired
    private ICoordinadorRepo coordinadorRepo;
    @Autowired
    private IParamMenuRepo paramMenuRepo;
    @Autowired
    private IPermisosSubMenuRepo permisosSubMenuRepo;
    @Autowired
    private IRolesPersonaRepo rolesPersonaRepository;
    @Autowired
    private IEstudiosPersonaRepo estudiosPersonaRepository;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private InscripcionRepo inscripcionesRepository;
    @Autowired
    private ConsolidacionRepoImpl consolidacionDao;
    @Autowired
    private IPagoService pagoService;
    @Autowired
    private ICursoService cursoService;
    @Autowired
    private InscripcionActividadRepo inscripcionActividadRepository;
    @Autowired
    private mapperMensaje mapperMensaje;
    @Autowired
    private UtilDate utilDate;
    @Autowired
    private JwtService jwtService;
    List<Persona> listPersonas;
    List<PersonaDto> listPersonasDto;
    public List<Persona> findAllUsuarios() {
return this.personaRepository.findUsuarios();
    }
    public Persona save(Persona persona) {
        return save(persona, 2);
    }
    public Persona save(Persona persona, int idRol) {
RolPersona rolPersona = new RolPersona();
persona.setPassword(encriptar(persona.getPassword()));
        persona.setPasswordHashVersion(1);
Persona personaSave = (Persona)this.personaRepository.save(persona);
rolPersona.setIdPersona(personaSave.getId().intValue());
rolPersona.setIdRol(idRol);
this.rolesPersonaRepository.save(rolPersona);
return personaSave;
    }
    public Persona update(Persona persona) {
Persona pUpdate = this.personaRepository.findByDocumento(persona.getDocumento());
persona.setPassword((pUpdate.getPassword() != null) ? pUpdate.getPassword() : "");
return (Persona)this.personaRepository.save(persona);
    }
    public Persona findPersonaById(Integer id) {
Optional<Persona> persona = this.personaRepository.findById(id);
return persona.get();
    }
    public Persona findPersonaByNombre(String nombre) {
Persona persona = this.personaRepository.findByNombre(nombre);
return persona;
    }
    public String delete(Persona persona) {
Persona personaSave = new Persona();
personaSave = this.personaRepository.findById(persona.getId()).get();
personaSave.setEstado(Boolean.valueOf(false));
this.personaRepository.save(personaSave);
return "asistente";
    }
    public Persona toggleActive(Integer idPersona, Boolean estado) {
Optional<Persona> personaSave = this.personaRepository.findById(idPersona);
((Persona)personaSave.get()).setEstado(estado);
Persona personaUpdate = (Persona)this.personaRepository.save(personaSave.get());
return personaUpdate;
    }
    public String habilitar(Persona persona) {
Persona personaSave = new Persona();
personaSave = this.personaRepository.findById(persona.getId()).get();
personaSave.setEstado(Boolean.valueOf(true));
this.personaRepository.save(personaSave);
return "asistente";
    }
    public PersonaDto buscarEmail(String email) {
Persona per = new Persona();
PersonaDto personadto = new PersonaDto();
      try {
per = this.personaRepository.findByEmail(email);
if (per != null) {
personadto.setNombre(per.getNombre());
personadto.setApellido(per.getApellido());
personadto.setDocumento(per.getDocumento());
personadto.setTipodocumento(per.getTipodocumento());
personadto.setEmail(per.getEmail());
personadto.setId(per.getId().intValue());
personadto.setFechanacimiento(per.getFechanacimiento());
personadto.setTelefono(per.getTelefono());
personadto.setPassword(per.getPassword());
personadto.setRoles(new ArrayList());
List<RolPersona> rol = new ArrayList<>();
List<Rol> roles = this.rolesDao.buscarRoles(Integer.valueOf(personadto.getId()));
for (Rol rolAsignado : roles) {
if (rolAsignado.getDescripcion().equalsIgnoreCase("ADMINISTRADOR")) {
personadto.getRoles().add(rolAsignado);
personadto.setAdmin(true);
personadto.setUser(false); continue;
            } 
personadto.getRoles().add(rolAsignado);
personadto.setAdmin(false);
personadto.setUser(true);
          }
        }
      }
catch (Exception e) {
e.printStackTrace();
      } 
return personadto;
    }
    public List<Persona> findAllByCurso(int idCurso) {
this.listPersonas = this.personaRepository.findPersonaByCurso(Integer.valueOf(idCurso));
return this.listPersonas;
    }
    public List<Persona> buscarTodosSinCurso(int idCurso) {
return this.personaRepository.findPersonaSinCurso(Integer.valueOf(idCurso));
    }
    public void eliminarPersonaCurso(int idPersona, int idCurso) {
      try {
this.personaRepository.deletePersonaConCurso(idPersona, idCurso);
} catch (Exception e) {
e.printStackTrace();
      } 
    }
    public void eliminarPersonaMinisterio(int idPersona, int idMinisterio) {
      try {
this.personaRepository.deletePersonaMinisterio(idPersona, idMinisterio);
} catch (Exception e) {
e.printStackTrace();
      } 
    }
    public void agregarPersonaCurso(int idPersona, int idCurso) {
      try {
Inscripciones inscripcion = new Inscripciones();
inscripcion.setIdCurso(idCurso);
inscripcion.setIdPersona(idPersona);
Inscripciones inscripciones1 = (Inscripciones)this.inscripcionesRepository.save(inscripcion);
      }
catch (Exception e) {
e.printStackTrace();
      } 
    }
    public void agregarPersonaActividad(int idPersona, int idActividad) {
      try {
InscripcionActividad inscripcion = new InscripcionActividad();
inscripcion.setIdActividad(idActividad);
inscripcion.setIdPersona(idPersona);
this.inscripcionActividadRepository.save(inscripcion);
      }
catch (Exception e) {
e.printStackTrace();
      } 
    }
    public PersonaDto buscarByDocumento(Integer doc) {
Persona per = new Persona();
PersonaDto personadto = new PersonaDto();
      try {
per = this.personaRepository.findByDocumento(doc);
if (per != null && per.getDocumento() != null) {
personadto.setNombre(per.getNombre());
personadto.setApellido(per.getApellido());
personadto.setDocumento(per.getDocumento());
personadto.setTipodocumento(per.getTipodocumento());
personadto.setEmail(per.getEmail());
personadto.setId(per.getId().intValue());
personadto.setFechanacimiento(per.getFechanacimiento());
personadto.setTelefono(per.getTelefono());
personadto.setPassword((per.getPassword() != null) ? per.getPassword() : "");
personadto.setRoles(new ArrayList());
List<RolPersona> rol = new ArrayList<>();
List<Rol> roles = this.rolesDao.buscarRoles(Integer.valueOf(personadto.getId()));
List<PermisosMenu> permisos = null;
for (Rol rolAsignado : roles) {
if (rolAsignado.getDescripcion().equalsIgnoreCase("ADMINISTRADOR")) {
permisos = this.menuService.findAllPermisosMenu(personadto.getId());
personadto.getRoles().add(rolAsignado);
personadto.setAdmin(true);
personadto.setUser(false);
personadto.setPermisosMenu(permisos);
            } else {
List<ParamMenu> menuList = this.paramMenuRepo.findAll();
personadto.getRoles().add(rolAsignado);
personadto.setAdmin(false);
personadto.setUser(true);
personadto.setPermisosMenu(contruirPermisosServidor(menuList));
            } 
personadto.setMensajes(this.mapperMensaje.listEntityToMensajesDTO(this.mensajesRepo.mesajesSinLeerByIdPersona(personadto.getId())));
personadto.setAsignacion(validateServiceByPersonAndDate(per.getId().intValue()));
          } 
        } else {
personadto = new PersonaDto();
        }
} catch (Exception e) {
this.LOGGER.error("[buscarByDocumento] " + e.getMessage());
e.printStackTrace();
personadto = new PersonaDto();
personadto.setId(1);
throw new RuntimeException("[buscarByDocumento]" + e.getMessage());
      } 
return personadto;
    }
    public boolean existeDocumento(Integer documento) {
return this.personaRepository.existsByDocumento(documento);
    }
    public boolean existeEmail(String email) {
return this.personaRepository.existsByEmail(email);
    }
    private void compararPermisos(List<PermisosMenu> permisos, int idPersona) {
List<ParamMenu> menuList = this.paramMenuRepo.findAll();
List<PermisosMenu> permisosCompletos = contruirPermisosServidor(menuList);
List<PermisosMenu> permisosActivos = new ArrayList<>();
for (PermisosMenu min : permisosCompletos) {
for (int i = 0; i < permisos.size(); i++) {
if (((PermisosMenu)permisos.get(i)).getMenu().getNombreBotonMenu() == min.getMenu().getNombreBotonMenu()) {
permisosActivos.add(min);
          }
        } 
      } 
permisosCompletos.removeAll(permisosActivos);
if (!permisosCompletos.isEmpty()) {
for (PermisosMenu permSave : permisosCompletos) {
PermisosMenu perm = new PermisosMenu();
perm.setIdPersona(idPersona);
perm.setNombreBotonMenu(permSave.getMenu().getNombreBotonMenu());
perm.setEstado("false");
perm.setMenu(permSave.getMenu());
this.permisosRepo.save(perm);
        } 
      }
    }
    private List<PermisosMenu> contruirPermisosServidor(List<ParamMenu> menuList) {
List<PermisosMenu> listPermisosIniciales = new ArrayList<>();
String estadoInicial = "false";
for (ParamMenu boton : menuList) {
PermisosMenu permisoInicial = new PermisosMenu();
permisoInicial.setIdPersona(1);
permisoInicial.setEstado(estadoInicial);
permisoInicial.setNombreBotonMenu(boton.getNombreBotonMenu());
permisoInicial.setMenu(boton);
listPermisosIniciales.add(permisoInicial);
      } 
return listPermisosIniciales;
    }
    public Persona saveAsistente(Persona persona) {
persona.setPassword("");
persona.setDiscapacidad((persona.getDiscapacidad() != null) ? persona.getDiscapacidad().booleanValue() : false);
persona.setPerteneceMinoria((persona.getPerteneceMinoria() != null) ? persona.getPerteneceMinoria().booleanValue() : false);
return (Persona)this.personaRepository.save(persona);
    }
    public Persona saveAsistenteConsolidacion(Persona persona, Consolidacion consolidacion) {
persona.setPassword("");
persona.setConsolidacion(Boolean.valueOf(true));
persona.setDiscapacidad((persona.getDiscapacidad() != null) ? persona.getDiscapacidad().booleanValue() : false);
persona.setPerteneceMinoria((persona.getPerteneceMinoria() != null) ? persona.getPerteneceMinoria().booleanValue() : false);
persona = (Persona)this.personaRepository.save(persona);
consolidacion.setIdPersona(persona.getId().intValue());
consolidacion.setAceptaConsolidacion(Boolean.valueOf((persona.getConsolidacion() != null) ? persona.getConsolidacion().booleanValue() : false));
this.iConsolidacionRepo.save(consolidacion);
return persona;
    }
    public List<PersonaDto> buscarConsolidacion(List<Persona> listaPersonas, int idCurso) {
List<PersonaDto> listaPersonasConsolidacion = new ArrayList<>();
listaPersonas.forEach(p -> listaPersonasConsolidacion.add(agregarConsolidacion(p, idCurso)));
return listaPersonasConsolidacion;
    }
    public PersonaDto agregarConsolidacion(Persona persona, int idCurso) {
PersonaDto perConsolidacion = new PersonaDto();
perConsolidacion.setNombre(persona.getNombre());
perConsolidacion.setApellido(persona.getApellido());
perConsolidacion.setDocumento(persona.getDocumento());
perConsolidacion.setEmail(persona.getEmail());
perConsolidacion.setFechanacimiento(persona.getFechanacimiento());
perConsolidacion.setId(persona.getId().intValue());
perConsolidacion.setTelefono(persona.getTelefono());
perConsolidacion.setTipodocumento(persona.getTipodocumento());
inscripcionConsolidacion inscripcionConsolidacion = this.consolidacionDao.listarConsolidacionByPersona(persona.getId().intValue());
if (inscripcionConsolidacion != null) {
perConsolidacion.setConsolidacion(true);
      } else {
perConsolidacion.setConsolidacion(false);
      } 
if (idCurso != 0) {
List<Pago> pagoList = this.pagoService.findPagosByIdCurso(persona.getId().intValue(), idCurso);
Curso curso = new Curso();
curso = this.cursoService.findCursoById(Integer.valueOf(idCurso));
int pagoTotal = 0;
int adeuda = 0;
for (Pago pago : pagoList) {
pagoTotal += pago.getValor();
        }
if (pagoTotal >= curso.getValorTotal()) {
perConsolidacion.setValidarPago(true);
        }
      } 
return perConsolidacion;
    }
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public String encriptar(String Pass) {
        return passwordEncoder.encode(Pass);
    }
    public Persona personaDtoToEntity(PersonaDto dto) {
Persona per = new Persona();
      try {
per.setNombre(dto.getNombre());
per.setApellido(dto.getApellido());
per.setDocumento(dto.getDocumento());
per.setTipodocumento(dto.getTipodocumento());
per.setEmail(dto.getEmail());
per.setId(Integer.valueOf(dto.getId()));
per.setFechanacimiento(dto.getFechanacimiento());
per.setTelefono(dto.getTelefono());
per.setPassword(dto.getPassword());
} catch (Exception e) {
e.printStackTrace();
      } 
return per;
    }
    public Persona savePassword(Persona persona) {
persona.setPassword(encriptar(persona.getPassword()));
        persona.setPasswordHashVersion(1);
return (Persona)this.personaRepository.save(persona);
    }
    public List<PersonaDto> findAllUsuariosRol() {
List<PersonaDto> listDto = new ArrayList<>();
List<Persona> personas = this.personaRepository.findUsuarios();
personas.forEach(p -> listDto.add(mapPersonaDto(p)));
return listDto;
    }
    public List<PersonaDto> findBirthdayByMonth() {
List<PersonaDto> listDto = new ArrayList<>();
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
ZonedDateTime nowInBogota = ZonedDateTime.now(ZoneId.of("America/Bogota"));
String fechaActual = nowInBogota.format(formatter);
DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
LocalDate ld = LocalDate.parse(fechaActual, dtf);
int month = ld.getMonthValue();
Integer diaactual = Integer.valueOf(ld.getDayOfMonth());
List<Object> obj = new ArrayList();
List<ServicioResponseDto> ListServicioDto = new ArrayList<>();
obj = this.personaRepository.buscarCumpleaños(month);
obj.forEach(p -> listDto.add(mapCumplePersonaDto(p, diaactual.toString())));
return listDto;
    }
    public List<PersonaDto> getBirthDay(List<PersonaDto> listDto) {
List<PersonaDto> listcCumpleActual = new ArrayList<>();
for (PersonaDto dto : listDto) {
if (dto.isCumpleActual()) {
listcCumpleActual.add(dto);
        }
      } 
return listcCumpleActual;
    }
    public List<PersonaReactDto> findAllUsers() {
List<PersonaReactDto> listpersonResult = new ArrayList<>();
List<Persona> listp = this.personaRepository.findAll(Sort.by(Sort.Direction.ASC, new String[] { "nombre" }));
listp.forEach(p -> listpersonResult.add(mapPersonaReactDto(p)));
return listpersonResult;
    }
    public UserResponseDto getUsuarioDesdeToken(String token) {
if (!this.jwtService.esTokenValido(token)) {
throw new RuntimeException("Token inválido o expirado");
      }
String cedula = this.jwtService.getCedulaDesdeToken(token);
Persona persona = this.personaRepository.findByDocumento(Integer.valueOf(Integer.parseInt(cedula)));
if (persona == null) {
throw new RuntimeException("Usuario no encontrado");
      }
UserResponseDto user = new UserResponseDto(persona);
user.setMinistry(this.ministerioRepo.findNameMinisterio(user.getId().intValue()));
return user;
    }
    private PersonaReactDto mapPersonaReactDto(Persona persona) {
PersonaReactDto dto = new PersonaReactDto();
      try {
dto.setId(persona.getId().intValue());
dto.setName(persona.getNombre() + " " + persona.getApellido());
dto.setEmail(persona.getEmail());
dto.setPhone(persona.getTelefono());
dto.setActive(persona.getEstado());
List<Rol> roles = this.rolesDao.buscarRoles(persona.getId());
if (!roles.isEmpty()) {
  Rol rolPrimero = roles.get(0);
  if (rolPrimero.getDescripcion().equalsIgnoreCase("ADMINISTRADOR")) {
    dto.setRole("ADMINISTRADOR");
  } else if (rolPrimero.getDescripcion().equalsIgnoreCase("SERVIDOR")) {
    dto.setRole("SERVIDOR");
  } else {
    dto.setRole("USUARIO");
  }
}
} catch (Exception e) {
e.printStackTrace();
      } 
return dto;
    }
    public void findUsuariosRol(int idPersona, int idRolNuevo) {
      List<RolPersona> rol = this.rolesPersonaRepository.findRolByidPersona(idPersona);
      RolPersona rolUpdate = rol.get(0);
      rolUpdate.setIdRol(idRolNuevo);
      this.rolesPersonaRepository.save(rolUpdate);
    }
    public List<EstudioPersona> findEstudiosPersona(int idPersona) {
List<EstudioPersona> estudios = this.estudiosPersonaRepository.findByIdPersona(idPersona);
return estudios;
    }
    public EstudioPersona saveEstudio(EstudioPersona estudioNew) {
EstudioPersona estudio = (EstudioPersona)this.estudiosPersonaRepository.save(estudioNew);
return estudio;
    }
    private PersonaDto mapPersonaDto(Persona persona) {
PersonaDto dto = new PersonaDto();
      try {
dto.setNombre(persona.getNombre());
dto.setApellido(persona.getApellido());
dto.setDocumento(persona.getDocumento());
dto.setTipodocumento(persona.getTipodocumento());
dto.setEmail(persona.getEmail());
dto.setId(persona.getId().intValue());
dto.setFechanacimiento(persona.getFechanacimiento());
dto.setTelefono(persona.getTelefono());
dto.setPassword(persona.getPassword());
List<Rol> roles = this.rolesDao.buscarRoles(persona.getId());
roles.forEach(r -> dto.setRolUnico(r));
dto.setRoles(roles);
} catch (Exception e) {
e.printStackTrace();
      } 
return dto;
    }
    private PersonaDto mapCumplePersonaDto(Object obj, String dia) {
PersonaDto dto = new PersonaDto();
Object[] object = (Object[])obj;
      try {
dto.setNombre(object[0].toString());
dto.setApellido(object[1].toString());
dto.setFechanacimiento(object[2].toString());
dto.setCumpleActual(dia.equals(object[2].toString()));
      }
catch (Exception e) {
e.printStackTrace();
      } 
return dto;
    }
    private List<AsignacionServicioDTO> validateServiceByPersonAndDate(int idPersona) {
List<AsignacionServicioDTO> asignacion = new ArrayList<>();
Optional<Date> fechaSiguiente = this.servicioRepo.findNextDateService();
Optional<List<Servicio>> ServiciosProximos = this.servicioRepo.findAllServiceByDateAndPerson(idPersona, fechaSiguiente.get());
if (ServiciosProximos.isPresent()) {
List<Servicio> listServices = ServiciosProximos.get();
listServices.forEach(serv -> {
              AsignacionServicioDTO asig = new AsignacionServicioDTO();
              Object objRepo = this.ministerioRepo.findMnisteryAndPosition(serv.getIdMinisterio(), serv.getIdPosicion());
              Object[] object = (Object[])objRepo;
              try {
                asig.setMInisterio(object[0].toString());
                asig.setPosiciones(object[1].toString());
                asignacion.add(asig);
} catch (Exception e) {
                e.printStackTrace();
              } 
            });
      } 
return asignacion;
    }
  }
