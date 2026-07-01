package  com.anunciadores.service;
import com.anunciadores.dto.*;
import com.anunciadores.enums.ECombos;
import com.anunciadores.mapper.mapperParametros;
import com.anunciadores.model.Coordinador;
import com.anunciadores.model.Ministerio;
import com.anunciadores.model.Persona;
import com.anunciadores.model.PersonaMinisterio;
import com.anunciadores.model.PosicionesMinisterio;
import com.anunciadores.model.Servicio;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import com.anunciadores.repository.*;
import com.anunciadores.service.interfaces.IServicioService;
import com.anunciadores.util.UtilDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
@Service
public class ServicioServiceImpl implements IServicioService {
    private Logger LOGGER = LoggerFactory.getLogger(ServicioServiceImpl.class);
    @Autowired
    private IMinisterioRepo ministerioRepository;
    @Autowired
    private IPosicionesRepo posicionesRepository;
    @Autowired
    private IServicioRepo servicioRepository;
    @Autowired
    private ICoordinadorRepo coordinadorRepo;
    @Autowired
    private IPersonaMinisterioRepo personaMinisterioRepoSitory;
    @Autowired
    private IPersonaRepo personaRepository;
    @Autowired
    private IParametrosRepo parametrosRepo;
    @Autowired
    private mapperParametros mapperParametros;
    @Autowired
    private UtilDate utilDate;
    public List<Ministerio> getAll() {
List<Ministerio> listaMinisterio = new ArrayList<>();
listaMinisterio = this.ministerioRepository.findAll();
return listaMinisterio;
    }
    public Ministerio saveMinisterio(String nombreMinisterio) {
Ministerio ministerio = new Ministerio();
ministerio.setNombre(nombreMinisterio);
return (Ministerio)this.ministerioRepository.save(ministerio);
    }
    public Ministerio saveMinisterio(MinisterioDto ministerioDto) {
Ministerio ministerio = new Ministerio();
ministerio.setNombre(ministerioDto.getNombreMinisterio());
ministerio.setId(ministerioDto.getId());
return (Ministerio)this.ministerioRepository.save(ministerio);
    }
    public void deleteMinisterio(int idMinisterio) {
this.ministerioRepository.deleteById(Integer.valueOf(idMinisterio));
    }
    public Ministerio findByidMnisterio(int idMinisterio) {
return this.ministerioRepository.findById(Integer.valueOf(idMinisterio)).get();
    }
    public List<PersonaDto> findPersonaByidMnisterio(int idMinisterio) {
List<PersonaDto> listaDto = new ArrayList<>();
List<Persona> listaEntity = this.ministerioRepository.findPersonasByIdMinisterio(idMinisterio);
listaEntity.forEach(p -> listaDto.add(mapPersonaToDto(p)));
return listaDto;
    }
    public List<PersonaDto> findPersonaByidMnisterioAsistencia(int idMinisterio) {
List<PersonaDto> listaDto = new ArrayList<>();
List<Persona> listaEntity = this.ministerioRepository.findPersonasByIdMinisterio(idMinisterio);
listaEntity.forEach(p -> listaDto.add(mapPersonaToDto(p)));
return listaDto;
    }
    public List<MinisterioDto> getPositionByidMinisterio(int idMinisterio) {
List<PosicionesMinisterio> listaPosiciones = new ArrayList<>();
List<MinisterioDto> listaPosicionesDto = new ArrayList<>();
Ministerio ministerio = this.ministerioRepository.findById(Integer.valueOf(idMinisterio)).get();
listaPosiciones = this.posicionesRepository.findAllByIdMinisterio(idMinisterio);
listaPosiciones.forEach(p -> listaPosicionesDto.add(mapEntityToDto(p, ministerio.getNombre())));
return listaPosicionesDto;
    }
    public List<MinisterioDto> getPositionByidMinisterioAndPerson(Date fechaActual, int idMinisterio) {
List<PosicionesMinisterio> listaPosiciones = new ArrayList<>();
List<MinisterioDto> listaPosicionesDto = new ArrayList<>();
List<Object> obj = new ArrayList();
List<ServicioResponseDto> ListServicioDto = new ArrayList<>();
obj = this.servicioRepository.findProgramacionByDateAndMinistery(fechaActual, idMinisterio);
Ministerio ministerio = this.ministerioRepository.findById(Integer.valueOf(idMinisterio)).get();
listaPosiciones = this.posicionesRepository.findAllByIdMinisterio(idMinisterio);
List<Object> finalObj = obj;
listaPosiciones.forEach(p -> listaPosicionesDto.add(mapEntityToDtoAndPerson(p, ministerio.getNombre(), finalObj)));
return listaPosicionesDto;
    }
    public List<MinisterioDto> limpiarListaPosiciones(List<MinisterioDto> posiciones, Date fechaServicio, int idMinisterio) {
List<MinisterioDto> listaResponse = new ArrayList<>();
List<Servicio> servicio = this.servicioRepository.findByFechaServicioAndIdMinisterio(fechaServicio, idMinisterio);
for (MinisterioDto min : posiciones) {
for (int i = 0; i < servicio.size(); i++) {
if (((Servicio)servicio.get(i)).getIdPosicion() == min.getPosicionDto().getId()) {
min.getPosicionDto().setAsistencia(((Servicio)servicio.get(i)).getAsistencia());
listaResponse.add(min);
                }
            }
        }
return listaResponse;
    }
    public List<PersonaDto> getPeopleWithoutMinisterio(int idMinisterio) {
List<PersonaDto> listaDto = new ArrayList<>();
List<Persona> listaEntity = this.ministerioRepository.findPeopleWithOutMinisterio(idMinisterio);
listaEntity.forEach(p -> listaDto.add(mapPersonaToDto(p)));
return listaDto;
    }
    public void saveProgramacion(ServicioDto servidores, Date fechaServicio, int idMinisterio) {
List<Servicio> progServicio = new ArrayList<>();
        try {
for (int i = 0; i < servidores.getPosicion().size(); i++) {
Servicio servicio = new Servicio();
List<PosicionesMinisterio> posicionEntity = this.posicionesRepository.findMinisterioByName(servidores.getPosicion().get(i), idMinisterio);
servicio.setIdPosicion(((PosicionesMinisterio)posicionEntity.get(0)).getId());
int posicion = Integer.parseInt(servidores.getEncargado().get(i));
servicio.setIdPersona(Integer.parseInt(servidores.getEncargado().get(i)));
servicio.setFechaServicio(fechaServicio);
servicio.setIdMinisterio(((PosicionesMinisterio)posicionEntity.get(0)).getIdMinisterio());
progServicio.add(servicio);
            }
this.servicioRepository.saveAll(progServicio);
} catch (Exception e) {
e.printStackTrace();
        }
    }
    public Boolean saveCoordinado(CoordinadorDTO cordinador) throws ParseException {
Boolean response = Boolean.valueOf(false);
Date fechaSave = this.utilDate.convertStringToDate(cordinador.getFechaString());
        try {
Coordinador cor = new Coordinador();
Optional<Coordinador> cord = this.coordinadorRepo.findByIdPersonaAndIdPersona(fechaSave, cordinador.getIdPersona());
if (!cord.isPresent()) {
cor.setPersona(this.personaRepository.findById(Integer.valueOf(cordinador.getIdPersona())).get());
cor.setFechaServicio(this.utilDate.convertStringToDate(cordinador.getFechaString()));
this.coordinadorRepo.save(cor);
response = Boolean.TRUE;
            }
} catch (Exception e) {
throw e;
        }
return response;
    }
    @Override
    public Boolean updateCoordinador(Coordinador cord, int idpersona) throws ParseException {
        Boolean response = Boolean.valueOf(false);
        try {
if (cord != null) {
cord.setPersona(this.personaRepository.findById(idpersona).get());
this.coordinadorRepo.save(cord);
response = Boolean.TRUE;
            }
} catch (Exception e) {
throw e;
        }
return response;
    }
    public Boolean saveCoordinadorEntity(CoordinadorDTO cordinador) {
        try {
Optional<Coordinador> CorOpt = this.coordinadorRepo.findByIdPersonaAndIdPersona(this.utilDate.convertStringToDate(cordinador.getFechaString()), cordinador.getPersona().getId().intValue());
if (CorOpt.isPresent()) {
((Coordinador)CorOpt.get()).setNotasServicio(cordinador.getNotasServicio());
this.coordinadorRepo.save(CorOpt.get());
return Boolean.valueOf(true);
            }
return Boolean.valueOf(false);
} catch (Exception e) {
return Boolean.valueOf(false);
        }
    }
    public Coordinador findCoordinador(List<ServicioListResponseDto> listProgramacionMinisterio) {
Coordinador cor = new Coordinador();
        try {
for (ServicioListResponseDto serv : listProgramacionMinisterio) {
if (serv.getFechaServcio() != null) {
SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
Date fechaDate = null;
                    try {
fechaDate = formato.parse(serv.getFechaServcio());
} catch (ParseException e) {
this.LOGGER.error("[findCoordinador] " + e.getMessage());
throw new RuntimeException("[findCoordinador] " + e.getMessage());
                    }
Coordinador cord = this.coordinadorRepo.findByFechaServicio(fechaDate);
return cord;
                }
            }
} catch (Exception e) {
this.LOGGER.error("[findCoordinador] " + e.getMessage());
throw new RuntimeException("[findCoordinador] " + e.getMessage());
        }
return cor;
    }
    public Coordinador findCoordinadorAdministrator(HttpServletRequest request) {
HttpSession misession = request.getSession();
Coordinador cor = new Coordinador();
Integer menuCordinador = Integer.valueOf(9);
Integer idpersona = Integer.valueOf(((Integer)misession.getAttribute("idPersona")).intValue());
        try {
Persona per = this.personaRepository.findPersonaAndIdMenu(idpersona.intValue(), menuCordinador.intValue());
cor.setId(per.getId().intValue());
cor.setPersona(per);
} catch (Exception e) {
e.getMessage();
        }
return cor;
    }
    public Coordinador findCoordinadorByFecha(Date fechaServicio) {
return this.coordinadorRepo.findByFechaServicio(fechaServicio);
    }
    public Coordinador findCoordinadorByFechaAndIdPersona(String fechaServicio, int idPersona) {
Date fechaDate = null;
SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
fechaDate = formato.parse(fechaServicio);
} catch (ParseException e) {
this.LOGGER.error("[findCoordinador] " + e.getMessage());
throw new RuntimeException("[findCoordinador] " + e.getMessage());
        }
Optional<Coordinador> CorOpt = this.coordinadorRepo.findByIdPersonaAndIdPersona(fechaDate, idPersona);
if (CorOpt.isPresent()) {
return CorOpt.get();
        }
return new Coordinador();
    }
    public Boolean validateCoordinadorByFechaAndIdPersona(String fechaServicio, int idPersona) {
Date fechaDate = null;
SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
fechaDate = formato.parse(fechaServicio);
} catch (ParseException e) {
this.LOGGER.error("[findCoordinador] " + e.getMessage());
throw new RuntimeException("[findCoordinador] " + e.getMessage());
        }
Optional<Coordinador> CorOpt = this.coordinadorRepo.findByIdPersonaAndIdPersona(fechaDate, idPersona);
if (CorOpt.isPresent()) {
return Boolean.valueOf(true);
        }
return Boolean.valueOf(false);
    }
    @Transactional
    public void updateProgramacion(ServicioDto servidores, Date fechaServicio, int idMinisterio) {
List<Servicio> progServicio = new ArrayList<>();
        try {
this.servicioRepository.deleteByFechaServicioAndIdMinisterio(fechaServicio, idMinisterio);
for (int i = 0; i < servidores.getPosicion().size(); i++) {
Servicio servicio = new Servicio();
List<PosicionesMinisterio> posicionEntity = this.posicionesRepository.findMinisterioByName(servidores.getPosicion().get(i), idMinisterio);
servicio.setFechaServicio(fechaServicio);
servicio.setIdMinisterio(idMinisterio);
servicio.setIdPersona(Integer.parseInt(servidores.getEncargado().get(i)));
servicio.setIdPosicion(((PosicionesMinisterio)posicionEntity.get(0)).getId());
progServicio.add(servicio);
            }
this.servicioRepository.saveAll(progServicio);
} catch (Exception e) {
e.printStackTrace();
        }
    }
    @Transactional
    public List<Persona> saveProgram(List<ServiceDTO> servicios) {
List<Persona> listmultipleAsiganacion = new ArrayList<>();
List<Servicio> progServicio = new ArrayList<>();
        try {
List<Servicio> list = this.servicioRepository.findByFechaServicioAndIdMinisterio(this.utilDate.convertLocaldateToDate(((ServiceDTO)servicios.get(0)).getFechaServicio()), Integer.parseInt(((ServiceDTO)servicios.get(0)).getIdMinisterio()));
if (!list.isEmpty()) {
list.forEach(s -> this.servicioRepository.deleteById(Integer.valueOf(s.getId())));
            }
for (int i = 0; i < servicios.size(); i++) {
Servicio servicio = new Servicio();
List<PosicionesMinisterio> posicionEntity = this.posicionesRepository.findMinisterioByName(((ServiceDTO)servicios.get(i)).getIdPosicion(), Integer.parseInt(((ServiceDTO)servicios.get(i)).getIdMinisterio()));
servicio.setFechaServicio(this.utilDate.convertLocaldateToDate(((ServiceDTO)servicios.get(i)).getFechaServicio()));
servicio.setIdMinisterio(Integer.parseInt(((ServiceDTO)servicios.get(i)).getIdMinisterio()));
servicio.setIdPersona(Integer.parseInt(((ServiceDTO)servicios.get(i)).getIdPersona()));
servicio.setIdPosicion(Integer.parseInt(((ServiceDTO)servicios.get(i)).getIdPosicion()));
progServicio.add(servicio);
Optional<Persona> per = validarPersonaProgramanda(servicio.getIdPersona(), servicio.getFechaServicio());
if (per.isPresent())
                {
listmultipleAsiganacion.add(per.get());
                }
            }
this.servicioRepository.saveAll(progServicio);
        }
catch (Exception e) {
throw new RuntimeException(e);
        }
return listmultipleAsiganacion;
    }
    private Optional<Persona> validarPersonaProgramanda(int idPersona, Date fechaServicio) {
        try {
Optional<Servicio> programacion = this.servicioRepository.findProgramacionServidor(idPersona, fechaServicio);
if (programacion.isPresent()) {
Optional<Persona> per = this.personaRepository.findById(Integer.valueOf(idPersona));
return per;
            }
} catch (Exception e) {
e.printStackTrace();
        }
return Optional.empty();
    }
    public void deleteProgramacion(Date fechaServicio) {}
    public Optional<Persona> validarProgramacionByFecha(ServicioDto servidores, Date fechaServicio) {
        try {
for (String servidor : servidores.getEncargado()) {
Optional<Servicio> programacion = this.servicioRepository.findProgramacionServidor(Integer.parseInt(servidor), fechaServicio);
if (programacion.isPresent()) {
Optional<Persona> per = this.personaRepository.findById(Integer.valueOf(Integer.parseInt(servidor)));
return per;
                }
            }
} catch (Exception e) {
e.printStackTrace();
        }
return Optional.empty();
    }
    public Optional<Persona> validarActualizarProgramacionByFecha(ServicioDto servidores, Date fechaServicio, int ministerio) {
        try {
for (String servidor : servidores.getEncargado()) {
Optional<Servicio> programacion = this.servicioRepository.findProgramacionServidor(Integer.parseInt(servidor), fechaServicio);
if (programacion.isPresent() && ((Servicio)programacion.get()).getIdMinisterio() != ministerio) {
Optional<Persona> per = this.personaRepository.findById(Integer.valueOf(Integer.parseInt(servidor)));
return per;
                }
            }
} catch (Exception e) {
e.printStackTrace();
        }
return Optional.empty();
    }
    public boolean validarActualizarProgramacionByFechaAndName(ServicioDto servidores, Date fechaServicio, int ministerio) {
        try {
for (int i = 0; i < servidores.getEncargado().size(); i++) {
Optional<Servicio> programacion = this.servicioRepository.findProgramacionServidorAndMinisterio(servidores.getEncargado().get(i), fechaServicio, servidores.getPosicion().get(i));
if (programacion.isPresent()) {
Servicio serSave = new Servicio();
serSave = programacion.get();
serSave.setAsistencia(servidores.getAsistencia().get(i));
serSave = (Servicio)this.servicioRepository.save(serSave);
                }
            }
} catch (Exception e) {
e.printStackTrace();
return false;
        }
return true;
    }
    public List<ServicioListResponseDto> findProgramacionByDate(Date fechaActual) {
List<ServicioListResponseDto> ListServiceDto = new ArrayList<>();
List<Object> obj = new ArrayList();
List<ServicioResponseDto> ListServicioDto = new ArrayList<>();
obj = this.servicioRepository.findProgramacionServicio(fechaActual);
if (!obj.isEmpty()) {
obj.forEach(O -> {
                try {
                    ListServicioDto.add(mapObjectToDto(O));
} catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            });
        }
ListServiceDto = buscarMinistarios(ListServicioDto);
return ListServiceDto;
    }
    public List<ServicioResponseDto> findProgramacionByDateAndMinisterio(Date fechaActual, int idMinisterio) {
List<ServicioListResponseDto> ListServiceDto = new ArrayList<>();
List<Object> obj = new ArrayList();
List<ServicioResponseDto> ListServicioDto = new ArrayList<>();
obj = this.servicioRepository.findProgramacionByDateAndidMinistery(fechaActual, idMinisterio);
int i = 0; if (i < 6)
        {
obj.forEach(O -> {
            try {
                ListServicioDto.add(mapObjectToDto(O));
} catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        }
ListServicioDto.size();
return ListServicioDto;
    }
    public List<ServicioListResponseDto> findProgramacionByDateGroup(Date fechaActual) throws ParseException {
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
List<ServicioListResponseDto> ListServiceDto = new ArrayList<>();
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
ZonedDateTime nowInBogota = ZonedDateTime.now(ZoneId.of("America/Bogota"));
String fechaActualStr = nowInBogota.format(formatter);
        try {
Date fechaActual1 = sdf.parse(fechaActualStr);
LocalDate fechaActualizada = nowInBogota.toLocalDate();
List<Object> obj = new ArrayList();
List<ServicioResponseDto> ListServicioDto = new ArrayList<>();
for (int i = 0; i < 6; i++) {
obj = this.servicioRepository.findProgramacionServicio(fechaActual);
if (obj.isEmpty()) {
fechaActualizada = fechaActualizada.plusDays(1L);
fechaActual = utilDate.convertLocaldateToDate(fechaActualizada);
                } else {
obj.forEach(O -> {
                        try {
                            ListServicioDto.add(mapObjectToDto(O));
} catch (ParseException e) {
                            this.LOGGER.error("[findProgramacionByDateGroup] " + e.getMessage());
                            throw new RuntimeException("[findProgramacionByDateGroup]" + e);
                        }
                    });
                    break;
                }
            }
ListServiceDto = buscarMinistarios(ListServicioDto);
} catch (Exception e) {
this.LOGGER.error("[findProgramacionByDateGroup] " + e.getMessage());
e.printStackTrace();
throw new RuntimeException("[findProgramacionByDateGroup]" + e);
        }
return ListServiceDto;
    }
    public List<ItemCombo> findItemsCombo() {
return this.mapperParametros.listEntitytoListDto(this.parametrosRepo.findByGrupo(ECombos.ASISTENCIA.toString()));
    }
    public void agregarPersonaAMinisterio(int idPersona, int idMinisterio) {
        try {
PersonaMinisterio personaSave = new PersonaMinisterio();
personaSave.setIdMinisterio(idMinisterio);
personaSave.setIdPersona(idPersona);
this.personaMinisterioRepoSitory.save(personaSave);
        }
catch (Exception e) {
e.printStackTrace();
        }
    }
    public void agregarPersonasAMinisterio(List<Long> listaPersonas, int idMinisterio) {
        try {
listaPersonas.forEach(p -> {
                PersonaMinisterio personaSave = new PersonaMinisterio();
                personaSave.setIdMinisterio(idMinisterio);
                personaSave.setIdPersona(p.intValue());
                this.personaMinisterioRepoSitory.save(personaSave);
            });
} catch (Exception e) {
e.printStackTrace();
        }
    }
    public List<ServicioResponseDto> buscarProgramacionMes(int idPersona) throws ParseException {
List<ServicioResponseDto> listaRespuestas = new ArrayList<>();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String fechaActual = sdf.format(new Date());
DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
LocalDate ld = LocalDate.parse(fechaActual, dtf);
int mes = 0;
        try {
for (int i = 0; i < 2; i++) {
if (i > 0) {
ld = ld.plusMonths(1L);
                }
int monthDays = ld.lengthOfMonth();
int yearDays = ld.lengthOfYear();
int year = ld.getYear();
int month = ld.getMonthValue();
System.out.printf("Mes % 4d de %d tiene %d días%nAño %d tiene %d días", new Object[] {
Integer.valueOf(month), Integer.valueOf(year), Integer.valueOf(monthDays),
Integer.valueOf(year), Integer.valueOf(yearDays)
                });
String fechainicial = "" + year + "-" + month + "-1";
String fechaFinal = "" + year + "-" + month + "-" + monthDays;
Date date1 = sdf.parse(fechainicial);
Date date2 = sdf.parse(fechaFinal);
if (month > mes) {
List<Servicio> servicio = this.servicioRepository.BuscarServicioMes(date1, date2, idPersona);
if (!servicio.isEmpty()) {
servicio.forEach(s -> listaRespuestas.add(buildServicioResponseDto(s)));
                    }
List<Coordinador> listaCoordinador = this.coordinadorRepo.buscarServicioCoordinadorMes(date1, date2, idPersona);
if (!listaCoordinador.isEmpty()) {
listaCoordinador.forEach(c -> listaRespuestas.add(buildServicioCoordinadorResponseDto(c)));
                    }
mes = month;
                }
if (!listaRespuestas.isEmpty()) {
listaRespuestas.sort(Comparator.comparing(ServicioResponseDto::getFechaServcio));
                }
            }
} catch (Exception exception) {}
return listaRespuestas;
    }
    private ServicioResponseDto buildServicioResponseDto(Servicio servicio) {
ServicioResponseDto serv = new ServicioResponseDto();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
serv.setFechaServcio(sdf.format(servicio.getFechaServicio()));
serv.setEncargado(((Persona)this.personaRepository.findById(Integer.valueOf(servicio.getIdPersona())).get()).getNombre());
serv.setPosicion(((PosicionesMinisterio)this.posicionesRepository.findById(Integer.valueOf(servicio.getIdPosicion())).get()).getNombrePosicion());
Optional<Ministerio> ministerio = this.ministerioRepository.findById(Integer.valueOf(servicio.getIdMinisterio()));
serv.setNombreMinisterio(((Ministerio)ministerio.get()).getNombre());
serv.setIdMinisterio(Integer.valueOf(((Ministerio)ministerio.get()).getId()));
return serv;
    }
    private ServicioResponseDto buildServicioCoordinadorResponseDto(Coordinador coordinador) {
ServicioResponseDto serv = new ServicioResponseDto();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
serv.setFechaServcio(sdf.format(coordinador.getFechaServicio()));
serv.setEncargado(coordinador.getPersona().getNombre());
serv.setPosicion("Coordinador");
serv.setNombreMinisterio("Coordinador");
return serv;
    }
    public void savePosicion(PosicionDto posicionDto) {
PosicionesMinisterio posicion = new PosicionesMinisterio();
posicion.setNombrePosicion(posicionDto.getNombrePosicion());
posicion.setIdMinisterio(posicionDto.getIdMinisterio());
this.posicionesRepository.save(posicion);
    }
    public void editPosicion(PosicionDto posicionDto) {
PosicionesMinisterio posicion = new PosicionesMinisterio();
posicion.setNombrePosicion(posicionDto.getNombrePosicion());
posicion.setIdMinisterio(posicionDto.getIdMinisterio());
posicion.setId(posicionDto.getId());
this.posicionesRepository.save(posicion);
    }
    public PosicionDto findPosicion(int idposicion) {
PosicionDto response = new PosicionDto();
PosicionesMinisterio posicion = this.posicionesRepository.findById(Integer.valueOf(idposicion)).get();
response.setIdMinisterio(posicion.getIdMinisterio());
response.setNombrePosicion(posicion.getNombrePosicion());
response.setId(posicion.getId());
return response;
    }
    public Boolean validarDuplicados(ServicioDto servidores) {
        try {
for (int i = 0; i < servidores.getEncargado().size(); i++) {
if (((String)servidores.getEncargado().get(i)).equals(Integer.valueOf(89898989))) {
System.out.println(servidores.getEncargado().get(i));
                }
            }
Set<String> set = new HashSet<>(servidores.getEncargado());
if (set.size() < servidores.getEncargado().size()) {
return Boolean.valueOf(false);
            }
return Boolean.valueOf(true);
        }
catch (Exception e) {
e.printStackTrace();
return Boolean.valueOf(false);
        }
    }
    public Persona identificarDuplicados(ServicioDto servidores) {
Persona[] pDuplicada = { new Persona() };
int cont = 0;
for (String e : servidores.getEncargado()) {
System.out.println("inicia ------> " + e);
cont = 0;
for (String enc : servidores.getEncargado()) {
if (enc.equals(e)) {
                    try {
cont++;
                    }
catch (Exception ex) {
ex.printStackTrace();
                    }
                }
System.out.println("interno enc " + enc + " " + cont);
if (cont > 1) {
return this.personaRepository.findById(Integer.valueOf(Integer.parseInt(e))).get();
                }
            }
        }
return pDuplicada[0];
    }
    public List<MinisterioDto> poblarPosiciones(List<MinisterioDto> ministerios, ServicioDto servicioDto) {
List<MinisterioDto> ministeriosFinal = new ArrayList<>();
ministerios.forEach(m -> {
            m.getPosicionDto().getNombrePosicion();
            for (int i = 0; i < servicioDto.getPosicion().size(); i++) {
                if (m.getPosicionDto().getNombrePosicion().equals(servicioDto.getPosicion().get(i))) {
                    PersonaDto perMinisterio = new PersonaDto();
                    perMinisterio.setId(Integer.parseInt(servicioDto.getEncargado().get(i)));
                    m.getPosicionDto().setPersonaDto(perMinisterio);
                } else if (!m.getPosicionDto().getNombrePosicion().equals(servicioDto.getPosicion().get(i)) && m.getPosicionDto().getPersonaDto() == null) {
                    PersonaDto perMinisterio = new PersonaDto();
                    perMinisterio.setId(0);
                    m.getPosicionDto().setPersonaDto(perMinisterio);
                }
            }
        });
ministeriosFinal = ministerios;
return ministeriosFinal;
    }
    public List<MinisterioDto> getPositionInitial(List<MinisterioDto> Ministerio) {
PersonaDto perPos = new PersonaDto();
perPos.setId(0);
Ministerio.forEach(o -> o.getPosicionDto().setPersonaDto(perPos));
return Ministerio;
    }
    public Persona getPersonDuplicate(ServicioDto servidores) {
Integer contador = Integer.valueOf(0);
Iterator<String> iterator = servidores.getEncargado().iterator(); if (iterator.hasNext()) { String o = iterator.next();
Optional<Persona> p = validarRepetido(o, servidores.getEncargado());
return p.get(); }
return new Persona();
    }
    public ProgramationDto findNextServices(Date fechaActual) throws ParseException {
ProgramationDto prog = new ProgramationDto();
List<MinistryDto> ministries = new ArrayList<>();
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
List<ServicioListResponseDto> ListServiceDto = new ArrayList<>();
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
ZonedDateTime nowInBogota = ZonedDateTime.now(ZoneId.of("America/Bogota"));
String fechaActualStr = nowInBogota.format(formatter);
        try {
Date fechaActual1 = sdf.parse(fechaActualStr);
LocalDate fechaActualizada = nowInBogota.toLocalDate();
List<ServicioResponseDto> ListServicioDto = new ArrayList<>();
for (int i = 0; i < 6; i++) {
Optional<List<Integer>> listMinisterios = this.servicioRepository.findDistinctIdMinisterio(fechaActual);
if (!listMinisterios.isPresent() || (listMinisterios.get()).isEmpty()) {
fechaActualizada = fechaActualizada.plusDays(1L);
fechaActual = utilDate.convertLocaldateToDate(fechaActualizada);
                } else {
Date finalFechaActual = fechaActual;
((List)listMinisterios.get()).forEach(min -> {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String fechaReactActualStr = null;
                        try {
                            fechaReactActualStr = this.utilDate.convertDateToStringWithFormat(finalFechaActual, "yyyy-MM-dd");
} catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        LocalDate ld = LocalDate.parse(fechaReactActualStr, dtf);
                        prog.setDate(ld);
                        Coordinador cord = this.coordinadorRepo.findByFechaServicio(finalFechaActual);
                        if (cord != null) {
                            CordinatorDto corDto = new CordinatorDto();
                            corDto.setName(cord.getPersona().getNombre());
                            corDto.setDate(ld);
                            corDto.setId(cord.getPersona().getId().intValue());
                            prog.setCoordinator(corDto);
                        }
                        List<MinistryMember> menbers = new ArrayList<>();
                        List<Object> resp = this.servicioRepository.findMInisteriesAndpositions(finalFechaActual, (Integer) min);
                        if (resp != null && resp.size() > 0) {
                            List<MinistryMember> positions = new ArrayList<>();
                            MinistryDto mDto = new MinistryDto();
                            Map<String, List<MinistryMember>> hashMin = new HashMap<>();
                            Object[] object = (Object[])resp.get(0);
                            mDto.setName(object[1].toString());
                            mDto.setId((Integer) min);
                            resp.forEach( m -> positions.add(mapPositionsDto(m)));
                            mDto.setPositions(positions);
                            ministries.add(mDto);
                        }
                    });
                    break;
                }
            }
ListServiceDto = buscarMinistarios(ListServicioDto);
} catch (Exception e) {
this.LOGGER.error("[findProgramacionByDateGroup] " + e.getMessage());
e.printStackTrace();
throw new RuntimeException("[findProgramacionByDateGroup]" + e);
        }
prog.setMinistries(ministries);
return prog;
    }
    public ProgramationDto findServices(Date fechaActual) throws ParseException {
ProgramationDto prog = new ProgramationDto();
List<MinistryDto> ministries = new ArrayList<>();
        try {
List<ServicioResponseDto> ListServicioDto = new ArrayList<>();
int i = 0; if (i < 6) {
Optional<List<Integer>> listMinisterios = this.servicioRepository.findDistinctIdMinisterio(fechaActual);
if (!listMinisterios.isPresent() || ((List)listMinisterios.get()).isEmpty()) {
return null;
                }
Date finalFechaActual = fechaActual;
((List)listMinisterios.get()).forEach(min -> {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String fechaReactActualStr = null;
                    try {
                        fechaReactActualStr = this.utilDate.convertDateToStringWithFormat(finalFechaActual, "yyyy-MM-dd");
} catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    LocalDate ld = LocalDate.parse(fechaReactActualStr, dtf);
                    prog.setDate(ld);
                    Coordinador cord = this.coordinadorRepo.findByFechaServicio(finalFechaActual);
                    if (cord != null) {
                        CordinatorDto corDto = new CordinatorDto();
                        corDto.setName(cord.getPersona().getNombre());
                        corDto.setDate(ld);
                        prog.setCoordinator(corDto);
                    }
                    List<MinistryMember> menbers = new ArrayList<>();
                    List<Object> resp = this.servicioRepository.findMInisteriesAndpositions(finalFechaActual, (Integer) min);
                    if (resp != null && resp.size() > 0) {
                        List<MinistryMember> positions = new ArrayList<>();
                        MinistryDto mDto = new MinistryDto();
                        //  resp.forEach(());
                        Map<String, List<MinistryMember>> hashMin = new HashMap<>();
                        Object[] object = (Object[])resp.get(0);
                        mDto.setName(object[1].toString());
                        mDto.setId((Integer) min);
                        resp.forEach( m -> positions.add(mapPositionsDto(m)));
                        mDto.setPositions(positions);
                        ministries.add(mDto);
                    }
                });
            }
} catch (Exception e) {
this.LOGGER.error("[findProgramacionByDateGroup] " + e.getMessage());
e.printStackTrace();
throw new RuntimeException("[findProgramacionByDateGroup]" + e);
        }
prog.setMinistries(ministries);
return prog;
    }
    private MinistryMember mapPositionsDto(Object obj) {
MinistryMember member = new MinistryMember();
Object[] object = (Object[])obj;
member.setMinistryName(object[1].toString());
member.setPositionId(((Integer)object[2]).intValue());
member.setPosition(object[3].toString());
member.setPersonName(object[4].toString());
member.setPersonId(((Integer)object[5]).intValue());
return member;
    }
    private Optional<Persona> validarRepetido(String idUser, List<String> listUsers) {
int c = 0;
for (int j = 0; j < listUsers.size(); j++) {
if (idUser.equalsIgnoreCase(listUsers.get(j))) {
c++;
if (c > 1) {
return this.personaRepository.findById(Integer.valueOf(Integer.parseInt(idUser)));
                }
            }
        }
return Optional.empty();
    }
    private MinisterioDto mapEntityToDto(PosicionesMinisterio Posicion, String nombreMinisterio) {
MinisterioDto dto = new MinisterioDto();
dto.setPosicionDto(new PosicionDto());
dto.setNombreMinisterio(nombreMinisterio);
dto.getPosicionDto().setIdMinisterio(Posicion.getIdMinisterio());
dto.getPosicionDto().setNombrePosicion(Posicion.getNombrePosicion());
dto.getPosicionDto().setId(Posicion.getId());
return dto;
    }
    private MinisterioDto mapEntityToDtoAndPerson(PosicionesMinisterio Posicion, String nombreMinisterio, List<Object> obj) {
MinisterioDto dto = new MinisterioDto();
dto.setPosicionDto(new PosicionDto());
dto.setNombreMinisterio(nombreMinisterio);
dto.getPosicionDto().setIdMinisterio(Posicion.getIdMinisterio());
dto.getPosicionDto().setNombrePosicion(Posicion.getNombrePosicion());
dto.getPosicionDto().setId(Posicion.getId());
obj.forEach(o -> {
            Object[] object = (Object[])o;
            Integer idPosicion = Integer.valueOf(Posicion.getId());
            if (idPosicion.equals(Integer.valueOf(Integer.parseInt(object[2].toString())))) {
                PersonaDto perMinisterio = new PersonaDto();
                perMinisterio.setId(Integer.parseInt(object[0].toString()));
                perMinisterio.setNombre(object[1].toString());
                dto.getPosicionDto().setPersonaDto(perMinisterio);
            }
        });
if (dto.getPosicionDto().getPersonaDto() == null) {
PersonaDto personaDto = new PersonaDto();
personaDto.setId(0);
dto.getPosicionDto().setPersonaDto(personaDto);
        }
return dto;
    }
    private PersonaDto mapPersonaToDto(Persona per) {
PersonaDto personadto = new PersonaDto();
personadto.setNombre(per.getNombre());
personadto.setApellido(per.getApellido());
personadto.setDocumento(per.getDocumento());
personadto.setTipodocumento(per.getTipodocumento());
personadto.setEmail(per.getEmail());
personadto.setId(per.getId().intValue());
personadto.setFechanacimiento(per.getFechanacimiento());
personadto.setTelefono(per.getTelefono());
personadto.setPassword(per.getPassword());
return personadto;
    }
    private ServicioResponseDto mapObjectToDto(Object obj) throws ParseException {
ServicioResponseDto servicioDto = new ServicioResponseDto("bateriavoz principal", "gaby ", "2023-12-04", "ministerio", Integer.valueOf(1));
Object[] object = (Object[])obj;
String date_s = object[0].toString();
SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
Date date1 = dt.parse(date_s);
SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
System.out.println(dt1.format(date1));
servicioDto.setFechaServcio(dt1.format(date1));
servicioDto.setEncargado(object[1].toString());
servicioDto.setPosicion(object[2].toString());
servicioDto.setIdMinisterio(Integer.valueOf(Integer.parseInt(object[3].toString())));
servicioDto.setNombreMinisterio(object[4].toString());
servicioDto.setIdNotificacionTelegram((object[5] != null) ? object[5].toString() : null);
servicioDto.setCelular((object[6] != null) ? object[6].toString() : null);
servicioDto.setAsistenciaList(this.mapperParametros.listEntitytoListDto(this.parametrosRepo.findByGrupo(ECombos.ASISTENCIA.toString())));
return servicioDto;
    }
    private List<ServicioListResponseDto> buscarMinistarios(List<ServicioResponseDto> ListServicioDto) {
List<ServicioListResponseDto> ListServiceDto = new ArrayList<>();
List<Ministerio> ministerioslist = this.ministerioRepository.findAll();
if (!ListServicioDto.isEmpty()) {
for (Ministerio m : ministerioslist) {
ServicioListResponseDto dtoResponse = new ServicioListResponseDto();
dtoResponse.setFechaServcio(((ServicioResponseDto)ListServicioDto.get(0)).getFechaServcio());
dtoResponse.setIdMinisterio(Integer.valueOf(m.getId()));
dtoResponse.setNombreMinisterio(m.getNombre());
dtoResponse.setServicioDTO((List)ListServicioDto
.stream().filter(p -> (p.getIdMinisterio().intValue() == m.getId()))
.collect(Collectors.toList()));
dtoResponse.setTamanoLista(Integer.valueOf(dtoResponse.getServicioDTO().size() + 1));
ListServiceDto.add(dtoResponse);
            }
        }
return ListServiceDto;
    }
    @Override
    public List<Servicio> getServiciosProximosPersona(int idPersona, Date fechaActual, Date fechaLimite) {
        try {
            return servicioRepository.findServiciosProximosPersona(idPersona, fechaActual, fechaLimite);
        } catch (Exception e) {
            LOGGER.error("Error al obtener servicios proximos", e);
            return new ArrayList<>();
        }
    }
    @Override
    public List<Servicio> getServiciosMesPersona(int idPersona, Date inicioMes, Date finMes) {
        try {
            return servicioRepository.findServiciosMesPersona(idPersona, inicioMes, finMes);
        } catch (Exception e) {
            LOGGER.error("Error al obtener servicios del mes", e);
            return new ArrayList<>();
        }
    }
    @Override
    public List<Object[]> getServiciosPorMinisterio(int idPersona, Date inicioMes, Date finMes) {
        try {
            return servicioRepository.countServiciosPorMinisterio(idPersona, inicioMes, finMes);
        } catch (Exception e) {
            LOGGER.error("Error al obtener servicios por ministerio", e);
            return new ArrayList<>();
        }
    }
}
