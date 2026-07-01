  package  com.anunciadores.dto;
  import com.anunciadores.dto.ItemCombo;
  import java.io.Serializable;
  import java.util.List;
  public class ServicioResponseDto
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
    private String posicion;
    private String encargado;
    private String fechaServcio;
    private String nombreMinisterio;
    private Integer idMinisterio;
    private String celular;
    private String idNotificacionTelegram;
    private List<ItemCombo> asistenciaList;
    public String getPosicion() {
return this.posicion;
    }
    public void setPosicion(String posicion) {
this.posicion = posicion;
    }
    public String getEncargado() {
return this.encargado;
    }
    public void setEncargado(String encargado) {
this.encargado = encargado;
    }
    public String getFechaServcio() {
return this.fechaServcio;
    }
    public void setFechaServcio(String fechaServcio) {
this.fechaServcio = fechaServcio;
    }
    public String getNombreMinisterio() {
return this.nombreMinisterio;
    }
    public void setNombreMinisterio(String nombreMinisterio) {
this.nombreMinisterio = nombreMinisterio;
    }
    public Integer getIdMinisterio() {
return this.idMinisterio;
    }
    public void setIdMinisterio(Integer idMinisterio) {
this.idMinisterio = idMinisterio;
    }
    public List<ItemCombo> getAsistenciaList() {
return this.asistenciaList;
    }
    public void setAsistenciaList(List<ItemCombo> asistenciaList) {
this.asistenciaList = asistenciaList;
    }
    public ServicioResponseDto(String posicion, String encargado, String fechaServcio, String nombreMinisterio, Integer idMinisterio) {
this.posicion = posicion;
this.encargado = encargado;
this.fechaServcio = fechaServcio;
this.nombreMinisterio = nombreMinisterio;
this.idMinisterio = idMinisterio;
    }
    public ServicioResponseDto() {}
    public String getIdNotificacionTelegram() {
return this.idNotificacionTelegram;
    }
    public void setIdNotificacionTelegram(String idNotificacionTelegram) {
this.idNotificacionTelegram = idNotificacionTelegram;
    }
    public String getCelular() {
return this.celular;
    }
    public void setCelular(String celular) {
this.celular = celular;
    }
  }
