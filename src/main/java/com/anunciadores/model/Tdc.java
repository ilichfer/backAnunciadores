  package  com.anunciadores.model;
  import java.io.Serializable;
  import java.util.Date;
  import javax.persistence.Column;
  import javax.persistence.Entity;
  import javax.persistence.GeneratedValue;
  import javax.persistence.GenerationType;
  import javax.persistence.Id;
  import javax.persistence.Lob;
  import javax.persistence.Table;
  @Entity
  @Table(name = "tdc")
  public class Tdc
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Lob
    @Column(name = "tdc", columnDefinition = "MEDIUMBLOB")
    private String tdc;
    @Column(name = "fecha_creacion")
    Date fechaCreacion;
    @Column(name = "id_persona")
    int idPersona;
    @Column(name = "nombre_documento")
    String nombredocumento;
    @Column(name = "pdf")
    Boolean pdf;
    @Column(name = "imagen")
    Boolean imagen;
    @Column(name = "url_image")
    String urlImage;
    public String getUrlImage() {
return this.urlImage;
    }
    public void setUrlImage(String urlImage) {
this.urlImage = urlImage;
    }
    public int getId() {
return this.id;
    }
    public void setId(int id) {
this.id = id;
    }
    public String getTdc() {
return this.tdc;
    }
    public void setTdc(String tdc) {
this.tdc = tdc;
    }
    public Date getFechaCreacion() {
      return fechaCreacion;
    }
    public void setFechaCreacion(Date fechaCreacion) {
this.fechaCreacion = fechaCreacion;
    }
    public int getIdPersona() {
return this.idPersona;
    }
    public void setIdPersona(int idPersona) {
this.idPersona = idPersona;
    }
    public String getNombredocumento() {
return this.nombredocumento;
    }
    public void setNombredocumento(String nombredocumento) {
this.nombredocumento = nombredocumento;
    }
    public Boolean getPdf() {
return this.pdf;
    }
    public void setPdf(Boolean pdf) {
this.pdf = pdf;
    }
    public Boolean getImagen() {
return this.imagen;
    }
    public void setImagen(Boolean imagen) {
this.imagen = imagen;
    }
  }
