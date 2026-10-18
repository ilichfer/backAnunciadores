package com.anunciadores.controller;

import com.anunciadores.dto.PersonaDto;
import com.anunciadores.dto.PersonaReactDto;
import com.anunciadores.dto.RegisterPersonaRequestDto;
import com.anunciadores.dto.ServicioListResponseDto;
import com.anunciadores.dto.UserResponseDto;
import com.anunciadores.model.Persona;
import com.anunciadores.model.Rol;
import com.anunciadores.security.CurrentUser;
import com.anunciadores.security.RateLimiter;
import com.anunciadores.service.interfaces.IPersonaService;
import com.anunciadores.service.interfaces.IServicioService;
import com.anunciadores.util.UtilDate;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.text.ParseException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Personas: usuario en sesion, listado, registro, roles, estado, contrasena y cumpleanos.
 */
@RestController
@RequestMapping("/api")
public class PersonaController {

    /** Registros publicos permitidos por IP en la ventana del RateLimiter (cubre jornadas de inscripcion). */
    private static final int MAX_REGISTROS_POR_IP = 30;

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private IServicioService servicioService;

    @Autowired
    private UtilDate utilDate;

    @Autowired
    private RateLimiter rateLimiter;

    @GetMapping("/user")
    public ResponseEntity<UserResponseDto> getUsuarioLogueado(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring("Bearer ".length());
        return ResponseEntity.ok(this.personaService.getUsuarioDesdeToken(token));
    }

    @GetMapping("/users")
    public ResponseEntity<List<PersonaReactDto>> users() throws JsonProcessingException, ParseException {
        return ResponseEntity.ok(this.personaService.findAllUsers());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Persona> getPersonaById(@PathVariable Integer id) {
        Persona persona = this.personaService.findPersonaById(id);
        if (persona == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada");
        }
        return ResponseEntity.ok(persona);
    }

    @GetMapping("/consutarEmail")
    public ResponseEntity<PersonaDto> consutarEmail(@RequestParam String email) throws JsonProcessingException {
        return ResponseEntity.ok(this.personaService.buscarEmail(email));
    }

    @GetMapping("/consutarDoc")
    public ResponseEntity<PersonaDto> consutarDoc(@RequestParam int doc) throws JsonProcessingException, ParseException {
        PersonaDto person = this.personaService.buscarByDocumento(doc);
        List<ServicioListResponseDto> programacion =
                this.servicioService.findProgramacionByDateGroup(this.utilDate.cargarfechaActualBogotaDate());
        if (!programacion.isEmpty()) {
            person.setCoordinadorActual(this.servicioService.validateCoordinadorByFechaAndIdPersona(
                    programacion.get(0).getFechaServcio(), person.getId()));
        } else {
            person.setCoordinadorActual(false);
        }
        return ResponseEntity.ok(person);
    }

    /**
     * Cambio de contrasena por cedula. Es publico a proposito: permite que las personas mayores
     * (o quien las ayude) recuperen el acceso de forma sencilla.
     */
    @PostMapping("/updatePassword")
    public ResponseEntity<?> updatePass(@RequestBody Persona persona) throws JsonProcessingException {
        PersonaDto per = this.personaService.buscarByDocumento(persona.getDocumento());
        if (per.getEmail() != null) {
            per.setPassword(persona.getPassword());
            persona = this.personaService.personaDtoToEntity(per);
            persona.setEstado(Boolean.TRUE);
            Persona personaSave = this.personaService.savePassword(persona);
            if (personaSave != null) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(personaSave);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar contraseña");
    }

    @GetMapping("/personas/{idpersona}/toggle-active")
    public ResponseEntity<?> toggleActive(@PathVariable Integer idpersona, @RequestParam boolean active) {
        Persona retorno = this.personaService.toggleActive(idpersona, active);
        if (retorno == null || retorno.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se pudo actualizar el estado del usuario");
        }
        return ResponseEntity.ok(retorno);
    }

    @PutMapping("/personas/{idPersona}/rol")
    public ResponseEntity<String> cambiarRol(@PathVariable Integer idPersona, @RequestParam Integer idRol) {
        String nombreRol;
        if (idRol == Rol.ID_ADMIN) {
            nombreRol = "ADMINISTRADOR";
        } else if (idRol == Rol.ID_USER) {
            nombreRol = "SERVIDOR";
        } else if (idRol == Rol.ID_USUARIO) {
            nombreRol = "USUARIO";
        } else {
            return ResponseEntity.badRequest().body("idRol debe ser " + Rol.ID_ADMIN + " (ADMIN), "
                    + Rol.ID_USER + " (SERVIDOR) o " + Rol.ID_USUARIO + " (USUARIO)");
        }
        this.personaService.findUsuariosRol(idPersona, idRol);
        return ResponseEntity.ok("Rol actualizado a " + nombreRol);
    }

    @GetMapping("/personas/todas")
    public ResponseEntity<List<PersonaDto>> todasLasPersonas() {
        return ResponseEntity.ok(this.personaService.findAllUsuariosRol());
    }

    /**
     * Registro de personas. Publico (formulario de la landing) o desde el panel de administracion.
     * Solo un ADMIN puede elegir el rol; en el registro publico siempre se asigna USUARIO.
     */
    @PostMapping("/personas/register")
    public ResponseEntity<?> registerPersona(@Valid @RequestBody RegisterPersonaRequestDto request,
                                             HttpServletRequest http) {
        boolean esAdmin = CurrentUser.isAdmin();
        if (!esAdmin && !rateLimiter.tryAcquire("registro:" + RateLimiter.clientIp(http), MAX_REGISTROS_POR_IP)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Se hicieron demasiados registros desde esta conexión. Intenta más tarde.");
        }
        if (this.personaService.existeDocumento(request.getDocumento())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe una persona registrada con el documento " + request.getDocumento());
        }
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()
                && this.personaService.existeEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe una persona registrada con el email " + request.getEmail());
        }

        Persona persona = new Persona();
        persona.setNombre(request.getNombre());
        persona.setApellido(request.getApellido());
        persona.setDocumento(request.getDocumento());
        persona.setTipodocumento(request.getTipodocumento());
        persona.setFechanacimiento(request.getFechanacimiento());
        persona.setGenero(request.getGenero());
        persona.setEstadoCivil(request.getEstadoCivil());
        persona.setEmail(request.getEmail());
        persona.setTelefono(request.getTelefono());
        persona.setCelular(request.getCelular());
        persona.setDireccion(request.getDireccion());
        persona.setCiudadDeptoDireccion(request.getCiudadDeptoDireccion());
        persona.setPaisNacimiento(request.getPaisNacimiento());
        persona.setCiudad(request.getCiudad());
        persona.setOcupacion(request.getOcupacion());
        persona.setEscolaridad(request.getEscolaridad());
        persona.setFechaConvercionCristo(request.getFechaConvercionCristo());
        persona.setFechaLlegadaAdc(request.getFechaLlegadaAdc());
        persona.setFechaBautizo(request.getFechaBautizo());
        persona.setFechaBautizoEspiritu(request.getFechaBautizoEspiritu());
        persona.setDiscapacidad(request.getDiscapacidad());
        persona.setDescDiscapacidad(request.getDescDiscapacidad());
        persona.setPerteneceMinoria(request.getPerteneceMinoria());
        persona.setDescMinoria(request.getDescMinoria());
        persona.setConsolidacion(request.getConsolidacion());
        persona.setPassword(request.getPassword() != null ? request.getPassword() : "");
        persona.setEstado(true);

        int idRol = Rol.ID_USUARIO;
        Integer rolSolicitado = request.getRol();
        if (esAdmin && rolSolicitado != null
                && (rolSolicitado == Rol.ID_ADMIN || rolSolicitado == Rol.ID_USER || rolSolicitado == Rol.ID_USUARIO)) {
            idRol = rolSolicitado;
        }
        Persona saved = this.personaService.save(persona, idRol);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/findBirthday")
    public ResponseEntity<List<PersonaDto>> findBirthday() throws ParseException {
        List<PersonaDto> cumpleanosMes = this.personaService.findBirthdayByMonth();
        return ResponseEntity.ok(this.personaService.getBirthDay(cumpleanosMes));
    }

    @GetMapping("/findBirthDaysMOnth")
    public ResponseEntity<List<PersonaDto>> findBirthDaysMOnth() throws ParseException {
        return ResponseEntity.ok(this.personaService.findBirthdayByMonth());
    }
}
