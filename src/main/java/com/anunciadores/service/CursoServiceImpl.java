  package  com.anunciadores.service;
  import com.anunciadores.dto.CursoDto;
  import com.anunciadores.dto.NotasCursoDTO;
  import com.anunciadores.dto.PersonaDto;
  import com.anunciadores.mapper.mapperNotas;
  import com.anunciadores.mapper.mapperPersona;
  import com.anunciadores.model.Curso;
  import com.anunciadores.model.NotasCurso;
  import com.anunciadores.model.Persona;
  import com.anunciadores.repository.ConsolidacionRepoImpl;
  import com.anunciadores.repository.CursosRepoImpl;
  import com.anunciadores.repository.ICursoRepo;
  import com.anunciadores.repository.INotasCursoRepo;
  import com.anunciadores.repository.IPersonaRepo;
  import com.anunciadores.service.interfaces.ICursoService;
  import java.text.DateFormat;
  import java.text.ParseException;
  import java.text.SimpleDateFormat;
  import java.util.ArrayList;
  import java.util.Comparator;
  import java.util.Date;
  import java.util.List;
  import java.util.Optional;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Service;
  @Service
  public class CursoServiceImpl
    implements ICursoService
  {
    @Autowired
    private ICursoRepo cursoRepository;
    @Autowired
    private IPersonaRepo personaRepo;
    @Autowired
    private INotasCursoRepo notasCursoRepo;
    @Autowired
    private CursosRepoImpl cursosRepository;
    @Autowired
    private ConsolidacionRepoImpl consolidacionDao;
    @Autowired
    private mapperNotas mapperNotas;
    @Autowired
    private mapperPersona mapperPersona;
List<Curso> listaCursos = new ArrayList<>();
    public List<Curso> findAll() {
List<Curso> listaCursosActivos = new ArrayList<>();
this.listaCursos = this.cursoRepository.findAll();
for (Curso curso : this.listaCursos) {
if (curso.isActivo()) {
listaCursosActivos.add(curso);
        }
      } 
return listaCursosActivos;
    }
    public List<Curso> findAllActive() {
List<Curso> listaCursosActivos = new ArrayList<>();
this.listaCursos = this.cursoRepository.findByActivo(true);
for (Curso curso : this.listaCursos) {
if (curso.isActivo()) {
listaCursosActivos.add(curso);
        }
      } 
return listaCursosActivos;
    }
    public List<Curso> findAllActiveByPerson(int idPersona) {
List<Curso> listaCursosActivos = new ArrayList<>();
this.listaCursos = this.cursoRepository.findByActivos(idPersona);
for (Curso curso : this.listaCursos) {
if (curso.isActivo()) {
listaCursosActivos.add(curso);
        }
      } 
return listaCursosActivos;
    }
    public Curso save(CursoDto curso) throws ParseException {
Curso cursoSave = new Curso();
List<Curso> list = this.cursoRepository.findTopByOrderByIdDesc();
cursoSave.setFechaFin(curso.getFechaFin());
cursoSave.setFechaInicio(curso.getFechaInicio());
cursoSave.setId(curso.getId());
cursoSave.setNombreCurso(curso.getNombreCurso());
cursoSave.setValorTotal(curso.getValorTotal());
cursoSave.setComentario((curso.getCheck() != null));
cursoSave.setActivo(true);
cursoSave.setProfesor(this.personaRepo.findById(Integer.valueOf(curso.getProfesor())).get());
return (Curso)this.cursoRepository.save(cursoSave);
    }
    public Curso findCursoById(Integer id) {
Optional<Curso> curso = this.cursoRepository.findById(id);
return curso.get();
    }
    public Curso delete(Curso curso) {
this.cursoRepository.deleteById(Integer.valueOf(curso.getId()));
return curso;
    }
    public NotasCurso findNotasByCurso(int idCurso, int idPersona) {
return this.notasCursoRepo.findNotasByCurso(idCurso, idPersona);
    }
    public List<NotasCurso> findHistoricoNotas(int idPersona) {
Optional<List<NotasCurso>> lista = this.notasCursoRepo.findHistoricoNotas(idPersona);
if (lista.isPresent()) {
return lista.get();
      }
List<NotasCurso> listaVacia = new ArrayList<>();
return listaVacia;
    }
    public NotasCurso saveNotasCurso(NotasCurso notas) throws ParseException {
double notaM = notas.getNotaMaestro() * 0.3D;
double notaA = notas.getNotaAsistencia() * 0.2D;
double notaP = notas.getNotaPractica() * 0.2D;
double notaEF = notas.getNotaExamenFinal() * 0.3D;
double notafinal = notaM + notaA + notaP + notaEF;
notas.setNotaFinal(notafinal);
return (NotasCurso)this.notasCursoRepo.save(notas);
    }
    public Date ParseFecha(String fecha) throws ParseException {
fecha = fecha.replace("-", "/");
SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
Date fechaDate = null;
fechaDate = formato.parse(fecha);
return fechaDate;
    }
    public String formatFecha(String fecha) throws ParseException {
DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
Date fechaDate = null;
fechaDate = dateFormat.parse(fecha);
Date date = new Date();
String dateToStr = dateFormat.format(fechaDate);
System.out.println("fechaInicio  formateada " + dateToStr);
return dateToStr;
    }
    public List<PersonaDto> buscarNotasXPersonas(int idCurso, List<Persona> estudiantes) {
List<PersonaDto> personasDto = this.mapperPersona.listEntityToConsolidacionDto(estudiantes);
personasDto.forEach(p -> p.setNotas(EntityToNotasCursoDTO(findNotasByCurso(idCurso, p.getId()))));
if (!personasDto.isEmpty()) {
personasDto.sort(Comparator.comparing(p -> Double.valueOf(p.getNotas().getNotaFinal())));
      }
return personasDto;
    }
    public List<Curso> findCursosByIdPersona(Integer idPersona) {
return this.cursosRepository.cursosByIdPersona(idPersona);
    }
    public List<CursoDto> findCursosDtoByIdPersona(Integer idPersona) {
List<CursoDto> cursosDto = new ArrayList<>();
List<Curso> listaCursos = this.cursoRepository.cursosByIdPersona(idPersona.intValue());
List<Curso> listaCursosDictados = this.cursoRepository.cursosByIdProfesor(idPersona.intValue());
if (listaCursosDictados != null && !listaCursosDictados.isEmpty()) {
listaCursosDictados.forEach(p -> listaCursos.add(p));
      }
listaCursos.forEach(c -> cursosDto.add(contruirCursosConsolidados(c, idPersona)));
return cursosDto;
    }
    private CursoDto contruirCursosConsolidados(Curso curso, Integer idPersona) {
CursoDto cursoDto = new CursoDto();
cursoDto.setId(curso.getId());
cursoDto.setIdPago(curso.getIdPago());
cursoDto.setFechaInicio(curso.getFechaInicio());
cursoDto.setFechaFin(curso.getFechaFin());
cursoDto.setNombreCurso(curso.getNombreCurso());
cursoDto.setValorTotal(curso.getValorTotal());
cursoDto.setProfesorDto(curso.getProfesor());
boolean activarNotas = false;
if (curso.getProfesor().getId() == idPersona) {
activarNotas = true;
      }
cursoDto.setNotas(activarNotas);
if (cursoDto.getNombreCurso().contains("padres")) {
cursoDto.setPersonaAConsolidar(buscarConsolidados(idPersona));
      }
return cursoDto;
    }
    private PersonaDto buscarConsolidados(Integer idPersona) {
PersonaDto personaDto = new PersonaDto();
Persona persona = this.consolidacionDao.listarPersonaConsolidacion(idPersona.intValue());
personaDto.setId(persona.getId().intValue());
personaDto.setNombre(persona.getNombre());
personaDto.setApellido(persona.getApellido());
return personaDto;
    }
    public Curso desactivarCurso(Curso curso) throws ParseException {
curso = this.cursoRepository.findById(Integer.valueOf(curso.getId())).get();
curso.setActivo(false);
return (Curso)this.cursoRepository.save(curso);
    }
    private NotasCursoDTO EntityToNotasCursoDTO(NotasCurso entity) {
if (entity == null) {
NotasCursoDTO notasCursoDTO1 = new NotasCursoDTO();
notasCursoDTO1.setNotaFinal(0.0D);
notasCursoDTO1.setColorCelda(2);
return notasCursoDTO1;
      } 
NotasCursoDTO notasCursoDTO = new NotasCursoDTO();
notasCursoDTO.setId(entity.getId());
notasCursoDTO.setCurso(entity.getCurso());
notasCursoDTO.setNotaMaestro(entity.getNotaMaestro());
notasCursoDTO.setNotaAsistencia(entity.getNotaAsistencia());
notasCursoDTO.setNotaPractica(entity.getNotaPractica());
notasCursoDTO.setNotaExamenFinal(entity.getNotaExamenFinal());
notasCursoDTO.setNotaFinal(entity.getNotaFinal());
if (notasCursoDTO != null && notasCursoDTO.getNotaFinal() >= 4.7D)
{ notasCursoDTO.setColorCelda(1); }
else if (notasCursoDTO != null && notasCursoDTO.getNotaFinal() < 3.0D)
{ notasCursoDTO.setColorCelda(2); }
else { notasCursoDTO.setColorCelda(3); }
return notasCursoDTO;
    }
  }
