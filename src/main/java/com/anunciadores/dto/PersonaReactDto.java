package  com.anunciadores.dto;
import java.io.Serializable;
public class PersonaReactDto
implements Serializable
{
private static final long serialVersionUID = 1L;
private int id;
private String name;
private String role;
private String ministry;
private String email;
private String phone;
private String avatar;
private Boolean active;
public int getId() {
return this.id;
}
public void setId(int id) {
this.id = id;
}
public String getName() {
return this.name;
}
public void setName(String name) {
this.name = name;
}
public String getRole() {
return this.role;
}
public void setRole(String role) {
this.role = role;
}
public String getMinistry() {
return this.ministry;
}
public void setMinistry(String ministry) {
this.ministry = ministry;
}
public String getEmail() {
return this.email;
}
public void setEmail(String email) {
this.email = email;
}
public String getPhone() {
return this.phone;
}
public void setPhone(String phone) {
this.phone = phone;
}
public String getAvatar() {
return this.avatar;
}
public void setAvatar(String avatar) {
this.avatar = avatar;
}
public Boolean getActive() {
return this.active;
}
public void setActive(Boolean active) {
this.active = active;
}
}
