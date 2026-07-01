package  com.anunciadores.auth.dto;
public class LoginResponse {
private String token;
private String rol;
private String nombre;
private Long id;
public LoginResponse(String token, String rol, String nombre, Long id) {
this.token = token;
this.rol = rol;
this.nombre = nombre;
this.id = id;
}
public String getToken() { return this.token; }
public String getRol() { return this.rol; }
public String getNombre() { return this.nombre; } public Long getId() {
return this.id;
}
}
