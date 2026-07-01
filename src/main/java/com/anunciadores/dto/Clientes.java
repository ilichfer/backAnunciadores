  package  com.anunciadores.dto;
  import java.io.Serializable;
  public class Clientes
    implements Serializable
  {
    private String tipoDocumento;
    private int Documento;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String celular;
    private String direccion;
    private String email;
    public String getPrimerNombre() {
return this.primerNombre;
    }
    public void setPrimerNombre(String primerNombre) {
this.primerNombre = primerNombre;
    }
    public String getSegundoNombre() {
return this.segundoNombre;
    }
    public void setSegundoNombre(String segundoNombre) {
this.segundoNombre = segundoNombre;
    }
    public String getPrimerApellido() {
return this.primerApellido;
    }
    public void setPrimerApellido(String primerApellido) {
this.primerApellido = primerApellido;
    }
    public String getSegundoApellido() {
return this.segundoApellido;
    }
    public void setSegundoApellido(String segundoApellido) {
this.segundoApellido = segundoApellido;
    }
    public String getCelular() {
return this.celular;
    }
    public void setCelular(String celular) {
this.celular = celular;
    }
    public String getDireccion() {
return this.direccion;
    }
    public void setDireccion(String direccion) {
this.direccion = direccion;
    }
    public String getEmail() {
return this.email;
    }
    public void setEmail(String email) {
this.email = email;
    }
    public String getTipoDocumento() {
return this.tipoDocumento;
    }
    public void setTipoDocumento(String tipoDocumento) {
this.tipoDocumento = tipoDocumento;
    }
    public int getDocumento() {
return this.Documento;
    }
    public void setDocumento(int documento) {
this.Documento = documento;
    }
  }
