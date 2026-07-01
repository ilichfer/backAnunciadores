  package  com.anunciadores.dto;
  import com.anunciadores.dto.AsignacionConsolidacionDto;
  import java.io.Serializable;
  import java.util.List;
  public class PersonaConsolidacionDto
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nombre;
    private String apellido;
    private Integer documento;
    private String telefono;
    private String fechanacimiento;
    private String tipodocumento;
    private String email;
    private String password;
    private String genero;
    private String estadoCivil;
    private String paisNacimiento;
    private String ciudad;
    private Boolean discapacidad;
    private String descDiscapacidad;
    private Boolean perteneceMinoria;
    private String descMinoria;
    private String direccion;
    private String ciudadDeptoDireccion;
    private String celular;
    private String ocupacion;
    private String escolaridad;
    private String fechaConvercionCristo;
    private String fechaLlegadaAdc;
    private String fechaBautizo;
    private String fechaBautizoEspiritu;
    private Boolean consolidacion;
    private List<AsignacionConsolidacionDto> asignacion;
    private Integer tamanoLista;
    public int getId() {
return this.id;
    }
    public void setId(int id) {
this.id = id;
    }
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
    public String getTelefono() {
return this.telefono;
    }
    public void setTelefono(String telefono) {
this.telefono = telefono;
    }
    public String getFechanacimiento() {
return this.fechanacimiento;
    }
    public void setFechanacimiento(String fechanacimiento) {
this.fechanacimiento = fechanacimiento;
    }
    public String getTipodocumento() {
return this.tipodocumento;
    }
    public void setTipodocumento(String tipodocumento) {
this.tipodocumento = tipodocumento;
    }
    public String getEmail() {
return this.email;
    }
    public void setEmail(String email) {
this.email = email;
    }
    public String getPassword() {
return this.password;
    }
    public void setPassword(String password) {
this.password = password;
    }
    public String getGenero() {
return this.genero;
    }
    public void setGenero(String genero) {
this.genero = genero;
    }
    public String getEstadoCivil() {
return this.estadoCivil;
    }
    public void setEstadoCivil(String estadoCivil) {
this.estadoCivil = estadoCivil;
    }
    public String getPaisNacimiento() {
return this.paisNacimiento;
    }
    public void setPaisNacimiento(String paisNacimiento) {
this.paisNacimiento = paisNacimiento;
    }
    public String getCiudad() {
return this.ciudad;
    }
    public void setCiudad(String ciudad) {
this.ciudad = ciudad;
    }
    public Boolean getDiscapacidad() {
return this.discapacidad;
    }
    public void setDiscapacidad(Boolean discapacidad) {
this.discapacidad = discapacidad;
    }
    public String getDescDiscapacidad() {
return this.descDiscapacidad;
    }
    public void setDescDiscapacidad(String descDiscapacidad) {
this.descDiscapacidad = descDiscapacidad;
    }
    public Boolean getPerteneceMinoria() {
return this.perteneceMinoria;
    }
    public void setPerteneceMinoria(Boolean perteneceMinoria) {
this.perteneceMinoria = perteneceMinoria;
    }
    public String getDescMinoria() {
return this.descMinoria;
    }
    public void setDescMinoria(String descMinoria) {
this.descMinoria = descMinoria;
    }
    public String getDireccion() {
return this.direccion;
    }
    public void setDireccion(String direccion) {
this.direccion = direccion;
    }
    public String getCiudadDeptoDireccion() {
return this.ciudadDeptoDireccion;
    }
    public void setCiudadDeptoDireccion(String ciudadDeptoDireccion) {
this.ciudadDeptoDireccion = ciudadDeptoDireccion;
    }
    public String getCelular() {
return this.celular;
    }
    public void setCelular(String celular) {
this.celular = celular;
    }
    public String getOcupacion() {
return this.ocupacion;
    }
    public void setOcupacion(String ocupacion) {
this.ocupacion = ocupacion;
    }
    public String getEscolaridad() {
return this.escolaridad;
    }
    public void setEscolaridad(String escolaridad) {
this.escolaridad = escolaridad;
    }
    public String getFechaConvercionCristo() {
return this.fechaConvercionCristo;
    }
    public void setFechaConvercionCristo(String fechaConvercionCristo) {
this.fechaConvercionCristo = fechaConvercionCristo;
    }
    public String getFechaLlegadaAdc() {
return this.fechaLlegadaAdc;
    }
    public void setFechaLlegadaAdc(String fechaLlegadaAdc) {
this.fechaLlegadaAdc = fechaLlegadaAdc;
    }
    public String getFechaBautizo() {
return this.fechaBautizo;
    }
    public void setFechaBautizo(String fechaBautizo) {
this.fechaBautizo = fechaBautizo;
    }
    public String getFechaBautizoEspiritu() {
return this.fechaBautizoEspiritu;
    }
    public void setFechaBautizoEspiritu(String fechaBautizoEspiritu) {
this.fechaBautizoEspiritu = fechaBautizoEspiritu;
    }
    public Boolean getConsolidacion() {
return this.consolidacion;
    }
    public void setConsolidacion(Boolean consolidacion) {
this.consolidacion = consolidacion;
    }
    public List<AsignacionConsolidacionDto> getAsignacion() {
return this.asignacion;
    }
    public void setAsignacion(List<AsignacionConsolidacionDto> asignacion) {
this.asignacion = asignacion;
    }
    public Integer getTamanoLista() {
return this.tamanoLista;
    }
    public void setTamanoLista(Integer tamanoLista) {
this.tamanoLista = tamanoLista;
    }
  }
