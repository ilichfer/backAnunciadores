package com.anunciadores.service;
import com.anunciadores.auth.dto.LoginRequest;
import com.anunciadores.auth.dto.LoginResponse;
import com.anunciadores.model.Persona;
import com.anunciadores.model.RolPersona;
import com.anunciadores.repository.IPersonaRepo;
import com.anunciadores.repository.IRolesPersonaRepo;
import com.anunciadores.service.JwtService;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class AuthService
{
  @Autowired
  private IPersonaRepo personaRepo;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private IRolesPersonaRepo rolesPersonaRepo;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  @Transactional
  public LoginResponse login(LoginRequest request) {
    Integer cedula;
    try {
      cedula = Integer.valueOf(Integer.parseInt(request.getCedula()));
    } catch (NumberFormatException e) {
      throw new RuntimeException("La cédula debe ser un número válido");
    }
    Persona persona = this.personaRepo.findByDocumento(cedula);
    if (persona == null) {
      throw new RuntimeException("Cédula o contraseña incorrectos");
    }
    if (persona.getEstado() == null || !persona.getEstado().booleanValue()) {
      throw new RuntimeException("Tu cuenta está inactiva. Contacta al administrador.");
    }
    boolean passwordValido;
    if (persona.getPasswordHashVersion() != null && persona.getPasswordHashVersion() == 1) {
      passwordValido = passwordEncoder.matches(request.getPassword(), persona.getPassword());
    } else {
      String passwordMd5 = toMd5(request.getPassword());
      passwordValido = passwordMd5.equals(persona.getPassword());
      if (passwordValido) {
        String bcryptHash = passwordEncoder.encode(request.getPassword());
        persona.setPassword(bcryptHash);
        persona.setPasswordHashVersion(1);
        personaRepo.save(persona);
      }
    }
    if (!passwordValido) {
      throw new RuntimeException("Cédula o contraseña incorrectos");
    }
    List<RolPersona> roldb = this.rolesPersonaRepo.findRolByidPersona(persona.getId().intValue());
    RolPersona rolP = roldb.get(0);
    String rol = (rolP.getIdRol() == 1) ? "ADMIN" : "USER";
    String nombreCompleto = persona.getNombre() + " " + persona.getApellido();
    String token = this.jwtService.generarToken(
        String.valueOf(persona.getDocumento()), rol, nombreCompleto);
    return new LoginResponse(token, rol, nombreCompleto, Long.valueOf(persona.getId().longValue()));
  }
  private String toMd5(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] messageDigest = md.digest(input.getBytes());
      BigInteger number = new BigInteger(1, messageDigest);
      String hash = number.toString(16);
      while (hash.length() < 32) {
        hash = "0" + hash;
      }
      return hash;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Error al procesar la contraseña");
    }
  }
}
