  package  com.anunciadores.dto;
  import com.anunciadores.dto.PersonaDto;
  import com.anunciadores.model.Persona;
  public class CursoDto
  {
    private int id;
    private int idPersona;
    private int idPago;
    private String nombreCurso;
    private String fechaInicio;
    private String fechaFin;
    private int valorTotal;
    private Boolean check;
    private PersonaDto personaAConsolidar;
    private int profesor;
    private Persona profesorDto;
    private boolean notas;
    public int getId() {
return this.id;
    }
    public void setId(int id) {
this.id = id;
    }
    public int getIdPersona() {
return this.idPersona;
    }
    public void setIdPersona(int idPersona) {
this.idPersona = idPersona;
    }
    public int getIdPago() {
return this.idPago;
    }
    public void setIdPago(int idPago) {
this.idPago = idPago;
    }
    public String getNombreCurso() {
return this.nombreCurso;
    }
    public void setNombreCurso(String nombreCurso) {
this.nombreCurso = nombreCurso;
    }
    public String getFechaInicio() {
return this.fechaInicio;
    }
    public void setFechaInicio(String fechaInicio) {
this.fechaInicio = fechaInicio;
    }
    public String getFechaFin() {
return this.fechaFin;
    }
    public void setFechaFin(String fechaFin) {
this.fechaFin = fechaFin;
    }
    public int getValorTotal() {
return this.valorTotal;
    }
    public void setValorTotal(int valorTotal) {
this.valorTotal = valorTotal;
    }
    public Boolean getCheck() {
return this.check;
    }
    public void setCheck(Boolean check) {
this.check = check;
    }
    public PersonaDto getPersonaAConsolidar() {
return this.personaAConsolidar;
    }
    public void setPersonaAConsolidar(PersonaDto personaAConsolidar) {
this.personaAConsolidar = personaAConsolidar;
    }
    public int getProfesor() {
return this.profesor;
    }
    public void setProfesor(int profesor) {
this.profesor = profesor;
    }
    public Persona getProfesorDto() {
return this.profesorDto;
    }
    public void setProfesorDto(Persona profesorDto) {
this.profesorDto = profesorDto;
    }
    public boolean isNotas() {
return this.notas;
    }
    public void setNotas(boolean notas) {
this.notas = notas;
    }
  }
