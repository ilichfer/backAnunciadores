package  com.anunciadores.auth.dto;
public class LoginRequest {
private String cedula;
private String password;
public String getCedula() { return this.cedula; } public void setCedula(String cedula) {
this.cedula = cedula;
}
public String getPassword() { return this.password; } public void setPassword(String password) {
this.password = password;
}
}
