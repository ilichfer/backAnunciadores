package  com.anunciadores.dto;
import com.anunciadores.model.Persona;
import java.util.List;
public class UserResponseDto
{
private Long id;
private String name;
private String role;
private String email;
private String phone;
private String avatar;
private List<String> ministry;
public UserResponseDto(Persona p) {
this.id = Long.valueOf(p.getId().longValue());
this.name = (p.getNombre() + " " + p.getApellido()).trim();
this.role = "ADMINISTRADOR";
this.email = p.getEmail();
this.phone = (p.getCelular() != null) ? p.getCelular() : p.getTelefono();
this.avatar = null;
this.ministry = List.of();
}
public Long getId() { return this.id; }
public String getName() { return this.name; }
public String getRole() { return this.role; }
public String getEmail() { return this.email; }
public String getPhone() { return this.phone; }
public String getAvatar() { return this.avatar; } public List<String> getMinistry() {
return this.ministry;
}
public void setId(Long id) {
this.id = id;
}
public void setName(String name) {
this.name = name;
}
public void setRole(String role) {
this.role = role;
}
public void setEmail(String email) {
this.email = email;
}
public void setPhone(String phone) {
this.phone = phone;
}
public void setAvatar(String avatar) {
this.avatar = avatar;
}
public void setMinistry(List<String> ministry) {
this.ministry = ministry;
}
}
