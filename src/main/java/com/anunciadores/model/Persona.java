  package  com.anunciadores.model;
  import java.io.Serializable;
  import javax.persistence.Column;
  import javax.persistence.Entity;
  import javax.persistence.GeneratedValue;
  import javax.persistence.GenerationType;
  import javax.persistence.Id;
  import javax.persistence.Table;
  @Entity
  @Table(name = "persona")
  public class Persona
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellido")
    private String apellido;
    @Column(name = "documento")
    private Integer documento;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "fechanacimiento")
    private String fechanacimiento;
    @Column(name = "tipodocumento")
    private String tipodocumento;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "password_hash_version")
    private Integer passwordHashVersion = 0;
    @Column(name = "genero")
    private String genero;
    @Column(name = "estado_civil")
    private String estadoCivil;
    @Column(name = "pais_nacimiento")
    private String paisNacimiento;
    @Column(name = "ciudad")
    private String ciudad;
    @Column(name = "discapacidad")
    private Boolean discapacidad;
    @Column(name = "descripcion_discapacidad")
    private String descDiscapacidad;
    @Column(name = "pertenece_minoria")
    private Boolean perteneceMinoria;
    @Column(name = "descripcion_minoria")
    private String descMinoria;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "ciudad_depto_direccion")
    private String ciudadDeptoDireccion;
    @Column(name = "celular")
    private String celular;
    @Column(name = "ocupacion")
    private String ocupacion;
    @Column(name = "escolaridad")
    private String escolaridad;
    @Column(name = "fecha_convercion_cristo")
    private String fechaConvercionCristo;
    @Column(name = "fecha_llegada_adc")
    private String fechaLlegadaAdc;
    @Column(name = "fecha_bautizo")
    private String fechaBautizo;
    @Column(name = "fecha_bautizo_espiritu")
    private String fechaBautizoEspiritu;
    @Column(name = "id_telegram")
    private String idTelegram;
    @Column(name = "consolidacion")
    private Boolean consolidacion;
    @Column(name = "estado")
    private Boolean estado;
    public Integer getId() {
return this.id;
    }
    public void setId(Integer id) {
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
    public Integer getPasswordHashVersion() {
        return passwordHashVersion;
    }
    public void setPasswordHashVersion(Integer passwordHashVersion) {
        this.passwordHashVersion = passwordHashVersion;
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
    public Boolean getPerteneceMinoria() {
return this.perteneceMinoria;
    }
    public void setPerteneceMinoria(Boolean perteneceMinoria) {
this.perteneceMinoria = perteneceMinoria;
    }
    public boolean isDiscapacidad() {
return this.discapacidad.booleanValue();
    }
    public void setDiscapacidad(boolean discapacidad) {
this.discapacidad = Boolean.valueOf(discapacidad);
    }
    public String getDescDiscapacidad() {
return this.descDiscapacidad;
    }
    public void setDescDiscapacidad(String descDiscapacidad) {
this.descDiscapacidad = descDiscapacidad;
    }
    public boolean isPerteneceMinoria() {
return this.perteneceMinoria.booleanValue();
    }
    public void setPerteneceMinoria(boolean perteneceMinoria) {
this.perteneceMinoria = Boolean.valueOf(perteneceMinoria);
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
    public Boolean getEstado() {
return this.estado;
    }
    public void setEstado(Boolean estado) {
this.estado = estado;
    }
    public String getIdTelegram() {
return this.idTelegram;
    }
    public void setIdTelegram(String idTelegram) {
this.idTelegram = idTelegram;
    }
  }
