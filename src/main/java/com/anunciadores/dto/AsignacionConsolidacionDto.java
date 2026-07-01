  package  com.anunciadores.dto;
  import java.io.Serializable;
  public class AsignacionConsolidacionDto
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
    private Integer idConsolidacion;
    private String nombre;
    private String telefono;
    private String apellido;
    private Integer documento;
    private int idPersona;
    private Boolean aceptaConsolidacion;
    private String horarioConsolidacionSugerido;
    private String horarioConsolidacionPersona;
    public String getNombre() {
return this.nombre;
    }
    public void setNombre(String nombre) {
this.nombre = nombre;
    }
    public String getApellido() {
return this.apellido;
    }
    public void setApellido(String apellido) {
this.apellido = apellido;
    }
    public Integer getDocumento() {
return this.documento;
    }
    public void setDocumento(Integer documento) {
this.documento = documento;
    }
    public int getIdPersona() {
return this.idPersona;
    }
    public void setIdPersona(int idPersona) {
this.idPersona = idPersona;
    }
    public Boolean getAceptaConsolidacion() {
return this.aceptaConsolidacion;
    }
    public void setAceptaConsolidacion(Boolean aceptaConsolidacion) {
this.aceptaConsolidacion = aceptaConsolidacion;
    }
    public String getHorarioConsolidacionSugerido() {
return this.horarioConsolidacionSugerido;
    }
    public void setHorarioConsolidacionSugerido(String horarioConsolidacionSugerido) {
this.horarioConsolidacionSugerido = horarioConsolidacionSugerido;
    }
    public String getHorarioConsolidacionPersona() {
return this.horarioConsolidacionPersona;
    }
    public void setHorarioConsolidacionPersona(String horarioConsolidacionPersona) {
this.horarioConsolidacionPersona = horarioConsolidacionPersona;
    }
    public String getTelefono() {
return this.telefono;
    }
    public void setTelefono(String telefono) {
this.telefono = telefono;
    }
    public Integer getIdConsolidacion() {
return this.idConsolidacion;
    }
    public void setIdConsolidacion(Integer idCosolidacion) {
this.idConsolidacion = idCosolidacion;
    }
  }
