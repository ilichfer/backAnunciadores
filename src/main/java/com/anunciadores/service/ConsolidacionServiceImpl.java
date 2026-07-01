  package  com.anunciadores.service;
  import com.anunciadores.dto.AsignacionConsolidacionDto;
  import com.anunciadores.dto.PersonaConsolidacionDto;
  import com.anunciadores.mapper.mapperConsolidacion;
  import com.anunciadores.model.Consolidacion;
  import com.anunciadores.model.HistoricoConsolidacion;
  import com.anunciadores.model.Ministerio;
  import com.anunciadores.model.Persona;
  import com.anunciadores.model.inscripcionConsolidacion;
  import com.anunciadores.repository.IConsolidacionRepo;
  import com.anunciadores.repository.IHisConsolidacionRepo;
  import com.anunciadores.repository.IInscripcionConsolidacionRepo;
  import com.anunciadores.repository.IMinisterioRepo;
  import com.anunciadores.repository.IPersonaRepo;
  import com.anunciadores.service.interfaces.IConsolidacionService;
  import java.util.ArrayList;
  import java.util.Date;
  import java.util.List;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Service;
  @Service
  public class ConsolidacionServiceImpl
    implements IConsolidacionService
  {
    @Autowired
    private IInscripcionConsolidacionRepo cosolidacionRepository;
    @Autowired
    private IMinisterioRepo ministerioRepo;
    @Autowired
    private mapperConsolidacion mapperConsolidacion;
    public Boolean asignarPersonaAPadreEspiritual(int idPadreEspiritual, int idPersonaConsolidar) {
Boolean asignacion = Boolean.FALSE;
List<inscripcionConsolidacion> insConsolidacion = this.inscripcionConsolidacionRepo.findByIdPadreEspiritual(idPadreEspiritual);
if (insConsolidacion.isEmpty()) {
inscripcionConsolidacion inscripcionConsolidacion = new inscripcionConsolidacion();
inscripcionConsolidacion.setIdPadreEspiritual(idPadreEspiritual);
inscripcionConsolidacion.setIdPersonaConsolidar(idPersonaConsolidar);
this.cosolidacionRepository.save(inscripcionConsolidacion);
asignacion = Boolean.TRUE;
      } 
return asignacion; } @Autowired
    private IInscripcionConsolidacionRepo inscripcionConsolidacionRepo; @Autowired
    private IPersonaRepo personaRepo; @Autowired
    private IConsolidacionRepo consolidacionRepo; @Autowired
    private IHisConsolidacionRepo hisConsolidacionRepo; public List<PersonaConsolidacionDto> listarservidoresConsolidacion(String nombreMInisterio) {
List<PersonaConsolidacionDto> servidoresConsolidacion = new ArrayList<>();
List<Ministerio> ministerios = this.ministerioRepo.findAll();
for (Ministerio min : ministerios) {
if (min.getNombre().equalsIgnoreCase(nombreMInisterio))
        {
servidoresConsolidacion = buscarAsignacion(this.mapperConsolidacion.liitEntitytoConsolidacionDto(this.ministerioRepo.findPersonasByIdMinisterio(min.getId())));
        }
      } 
return servidoresConsolidacion;
    }
    public List<AsignacionConsolidacionDto> listarConsolidacionByServidor(int idServidor) {
List<AsignacionConsolidacionDto> asignacionlist = new ArrayList<>();
List<Persona> personasList = this.inscripcionConsolidacionRepo.findConsolidacionesByServidor(idServidor);
if (!personasList.isEmpty()) {
for (Persona per : personasList) {
AsignacionConsolidacionDto dto = new AsignacionConsolidacionDto();
Consolidacion con = this.consolidacionRepo.findByIdPersona(per.getId().intValue());
dto.setNombre(per.getNombre());
dto.setApellido(per.getApellido());
dto.setTelefono(per.getTelefono());
dto.setHorarioConsolidacionSugerido(con.getHorarioConsolidacionSugerido());
dto.setHorarioConsolidacionPersona(con.getHorarioConsolidacionPersona());
dto.setIdConsolidacion(Integer.valueOf(con.getId()));
dto.setIdPersona(idServidor);
asignacionlist.add(dto);
        } 
return asignacionlist;
      } 
return null;
    }
    public void saveDescripcionConsolidacion(int idConsolidacion, String DescConsolidacion) {
HistoricoConsolidacion his = new HistoricoConsolidacion();
his.setFechaRegistroConsolidacion(new Date());
his.setIdConsolidacion(idConsolidacion);
his.setDescripcionConsolidacion(DescConsolidacion);
this.hisConsolidacionRepo.save(his);
    }
    private List<PersonaConsolidacionDto> buscarAsignacion(List<PersonaConsolidacionDto> servidoresConsolidacion) {
for (PersonaConsolidacionDto per : servidoresConsolidacion) {
List<AsignacionConsolidacionDto> asignacionlist = new ArrayList<>();
List<inscripcionConsolidacion> insConsolidacion = this.inscripcionConsolidacionRepo.findByIdPadreEspiritual(per.getId());
if (insConsolidacion.isEmpty()) {
AsignacionConsolidacionDto asignacionConsolidacionDto = new AsignacionConsolidacionDto();
asignacionConsolidacionDto.setNombre("N/A");
asignacionConsolidacionDto.setApellido("");
asignacionConsolidacionDto.setDocumento(Integer.valueOf(0));
asignacionConsolidacionDto.setHorarioConsolidacionPersona("N/A");
asignacionConsolidacionDto.setTelefono("N/A");
asignacionConsolidacionDto.setHorarioConsolidacionPersona("N/A");
asignacionConsolidacionDto.setHorarioConsolidacionSugerido("N/A");
asignacionlist.add(asignacionConsolidacionDto);
per.setAsignacion(asignacionlist);
per.setTamanoLista(Integer.valueOf(asignacionlist.size() + 1));
          continue;
        } 
AsignacionConsolidacionDto dto = new AsignacionConsolidacionDto();
Persona perAsignacion = this.personaRepo.findById(Integer.valueOf(((inscripcionConsolidacion)insConsolidacion.get(0)).getIdPersonaConsolidar())).get();
Consolidacion con = this.consolidacionRepo.findByIdPersona(((inscripcionConsolidacion)insConsolidacion.get(0)).getIdPersonaConsolidar());
dto.setNombre(perAsignacion.getNombre());
dto.setApellido(perAsignacion.getApellido());
dto.setDocumento(perAsignacion.getDocumento());
dto.setTelefono(perAsignacion.getTelefono());
dto.setHorarioConsolidacionPersona(con.getHorarioConsolidacionPersona());
dto.setHorarioConsolidacionSugerido(con.getHorarioConsolidacionSugerido());
asignacionlist.add(dto);
per.setAsignacion(asignacionlist);
per.setTamanoLista(Integer.valueOf(asignacionlist.size() + 1));
      } 
return servidoresConsolidacion;
    }
  }
