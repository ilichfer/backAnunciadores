  package  com.anunciadores.service;
  import com.anunciadores.auth.dto.assignments;
  import com.anunciadores.auth.dto.updateServiceRequest;
  import com.anunciadores.dto.Ministry;
  import com.anunciadores.dto.PositionDto;
  import com.anunciadores.mapper.mapperParametros;
  import com.anunciadores.model.Ministerio;
  import com.anunciadores.model.PersonaMinisterio;
  import com.anunciadores.model.PosicionesMinisterio;
  import com.anunciadores.model.Servicio;
  import com.anunciadores.repository.ICoordinadorRepo;
  import com.anunciadores.repository.IMinisterioRepo;
  import com.anunciadores.repository.IParametrosRepo;
  import com.anunciadores.repository.IPersonaMinisterioRepo;
  import com.anunciadores.repository.IPersonaRepo;
  import com.anunciadores.repository.IPosicionesRepo;
  import com.anunciadores.repository.IServicioRepo;
  import com.anunciadores.service.interfaces.IMInisteryService;
  import com.anunciadores.util.UtilDate;
  import java.text.ParseException;
  import java.util.ArrayList;
  import java.util.Date;
  import java.util.List;
  import java.util.Optional;
  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Service;
  import org.springframework.transaction.annotation.Transactional;
  @Service
  public class MinistryServiceImpl implements IMInisteryService {
private Logger LOGGER = LoggerFactory.getLogger(com.anunciadores.service.MinistryServiceImpl.class);
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
    public List<Ministry> getAllministriesWithPositions() {
List<Ministry> ministryList = new ArrayList<>();
List<Ministerio> listMin = this.ministerioRepository.findAll();
if (listMin != null && listMin.size() > 0) {
listMin.forEach(m -> {
              Ministry min = new Ministry();
              List<PosicionesMinisterio> listpositions = this.posicionesRepository.findAllByIdMinisterio(m.getId());
              min = new Ministry();
              min.setId(String.valueOf(m.getId()));
              min.setName(m.getNombre());
              List<PositionDto> listP = new ArrayList<>();
              //listpositions.forEach(());
                listpositions.forEach(p -> listP.add(createPosition(p)));
                min.setPositions(listP);
              ministryList.add(min);
            });
      }
return ministryList;
    }
    private PositionDto createPosition(PosicionesMinisterio p) {
PositionDto position = new PositionDto();
position.setId(p.getId());
position.setName(p.getNombrePosicion());
return position;
    }
    public void agregarPersonasAMinisterio(int idPersona, int idMinisterio) {
      try {
PersonaMinisterio personaSave = new PersonaMinisterio();
personaSave.setIdMinisterio(idMinisterio);
personaSave.setIdPersona(idPersona);
this.personaMinisterioRepoSitory.save(personaSave);
} catch (Exception e) {
e.printStackTrace();
      } 
    }
    public Ministry getMinistryWithPositions(Integer idMinisterio) {
List<Ministry> ministryList = new ArrayList<>();
Optional<Ministerio> ministerio = this.ministerioRepository.findById(idMinisterio);
Ministry min = new Ministry();
if (ministerio.isPresent()) {
List<PosicionesMinisterio> listpositions = this.posicionesRepository.findAllByIdMinisterio(((Ministerio)ministerio.get()).getId());
min = new Ministry();
min.setId(String.valueOf(((Ministerio)ministerio.get()).getId()));
min.setName(((Ministerio)ministerio.get()).getNombre());
List<PositionDto> listP = new ArrayList<>();
listpositions.forEach(p -> listP.add(createPosition(p)));
min.setPositions(listP);
      } 
return min;
    }
    @Transactional
    public void updateService(updateServiceRequest updateRequest) throws ParseException {
Date fechaaServicio = this.utilDate.convertStringToDate(updateRequest.getDate());
this.servicioRepository.deleteByFechaServicioAndIdMinisterio(fechaaServicio, updateRequest.getMinistryId());
List<Servicio> progServicio = new ArrayList<>();
for (int i = 0; i < updateRequest.getAssignments().size(); i++) {
if (((assignments)updateRequest.getAssignments().get(i)).getPersonId() != 0) {
Servicio servicio = new Servicio();
PosicionesMinisterio posicionEntity = this.posicionesRepository.findMinisterioByName(((assignments)updateRequest.getAssignments().get(i)).getIdPosicion(), updateRequest.getMinistryId());
servicio.setFechaServicio(this.utilDate.convertStringToDate(updateRequest.getDate()));
servicio.setIdMinisterio(updateRequest.getMinistryId());
servicio.setIdPersona(((assignments)updateRequest.getAssignments().get(i)).getPersonId());
servicio.setIdPosicion(((assignments)updateRequest.getAssignments().get(i)).getIdPosicion());
progServicio.add(servicio);
        } 
this.servicioRepository.saveAll(progServicio);
      } 
    }
  }
