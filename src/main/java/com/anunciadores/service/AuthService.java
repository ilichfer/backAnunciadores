package com.anunciadores.service;

import com.anunciadores.auth.dto.LoginException;
import com.anunciadores.auth.dto.LoginRequest;
import com.anunciadores.auth.dto.LoginResponse;
import com.anunciadores.model.Persona;
import com.anunciadores.model.Rol;
import com.anunciadores.model.RolPersona;
import com.anunciadores.repository.IPersonaRepo;
import com.anunciadores.repository.IRolesPersonaRepo;
import com.anunciadores.security.AuthenticatedUser;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    private static final String CREDENCIALES_INVALIDAS = "Cédula o contraseña incorrectos";

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
            cedula = Integer.valueOf(request.getCedula().trim());
        } catch (NumberFormatException e) {
            throw new LoginException("La cédula debe ser un número válido");
        }

        Persona persona = this.personaRepo.findByDocumento(cedula);
        if (persona == null) {
            throw new LoginException(CREDENCIALES_INVALIDAS);
        }
        if (persona.getEstado() == null || !persona.getEstado()) {
            throw new LoginException("Tu cuenta está inactiva. Contacta al administrador.");
        }

        boolean passwordValido;
        if (persona.getPasswordHashVersion() != null && persona.getPasswordHashVersion() == 1) {
            passwordValido = passwordEncoder.matches(request.getPassword(), persona.getPassword());
        } else {
            // Contrasenas antiguas en MD5: si coinciden se migran a BCrypt en este mismo login
            passwordValido = toMd5(request.getPassword()).equals(persona.getPassword());
            if (passwordValido) {
                persona.setPassword(passwordEncoder.encode(request.getPassword()));
                persona.setPasswordHashVersion(1);
                personaRepo.save(persona);
            }
        }
        if (!passwordValido) {
            throw new LoginException(CREDENCIALES_INVALIDAS);
        }

        String rol = resolverRol(persona);
        String nombreCompleto = persona.getNombre() + " " + persona.getApellido();
        String token = this.jwtService.generarToken(
                persona.getId(), String.valueOf(persona.getDocumento()), rol, nombreCompleto);
        return new LoginResponse(token, rol, nombreCompleto, Long.valueOf(persona.getId().longValue()));
    }

    /** Si la persona no tiene rol asignado entra con el rol de menor privilegio. */
    private String resolverRol(Persona persona) {
        List<RolPersona> roles = this.rolesPersonaRepo.findRolByidPersona(persona.getId());
        if (roles == null || roles.isEmpty()) {
            LOGGER.warn("La persona {} no tiene rol asignado; se usa USUARIO", persona.getId());
            return AuthenticatedUser.ROL_USUARIO;
        }
        int idRol = roles.get(0).getIdRol();
        if (idRol == Rol.ID_ADMIN) {
            return AuthenticatedUser.ROL_ADMIN;
        }
        if (idRol == Rol.ID_USER) {
            return AuthenticatedUser.ROL_SERVIDOR;
        }
        return AuthenticatedUser.ROL_USUARIO;
    }

    private String toMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder(new BigInteger(1, messageDigest).toString(16));
            while (hash.length() < 32) {
                hash.insert(0, '0');
            }
            return hash.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 no disponible", e);
        }
    }
}
