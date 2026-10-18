package com.anunciadores.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.anunciadores.auth.dto.LoginException;
import com.anunciadores.controller.AuthController;
import com.anunciadores.controller.ContactoController;
import com.anunciadores.controller.NotificacionController;
import com.anunciadores.controller.PersonaController;
import com.anunciadores.controller.TcdController;
import com.anunciadores.model.Persona;
import com.anunciadores.model.Rol;
import com.anunciadores.repository.INotificacionRepo;
import com.anunciadores.service.AuthService;
import com.anunciadores.service.JwtService;
import com.anunciadores.service.interfaces.IContactoService;
import com.anunciadores.service.interfaces.INotificacionService;
import com.anunciadores.service.interfaces.IPersonaService;
import com.anunciadores.service.interfaces.IR2UploadService;
import com.anunciadores.service.interfaces.IServicioService;
import com.anunciadores.service.interfaces.ITdcService;
import com.anunciadores.util.UtilDate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Verifica las reglas de acceso de la API sin necesitar base de datos.
 */
@WebMvcTest(controllers = {AuthController.class, PersonaController.class, NotificacionController.class,
        ContactoController.class, TcdController.class})
@Import({SecurityConfig.class, RateLimiter.class, JwtService.class})
@TestPropertySource(properties = {
        "security.jwt.secret=clave-de-pruebas-con-mas-de-32-caracteres-123",
        "security.jwt.issuer=anunciadores",
        "security.jwt.ttlMillis=60000",
        "security.cors.allowed-origins=http://localhost:3000",
        "security.rate-limit.max-attempts=3",
        "security.rate-limit.window-minutes=15"
})
class SecurityRulesTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;
    @MockBean
    private IPersonaService personaService;
    @MockBean
    private IServicioService servicioService;
    @MockBean
    private UtilDate utilDate;
    @MockBean
    private INotificacionService notificacionService;
    @MockBean
    private INotificacionRepo notificacionRepo;
    @MockBean
    private IContactoService contactoService;
    @MockBean
    private ITdcService tdcService;
    @MockBean
    private IR2UploadService r2UploadService;
    @MockBean
    private com.anunciadores.service.AuditoriaService auditoriaService;
    @MockBean
    private com.anunciadores.service.TcdRachaService tcdRachaService;

    private String token(int id, String rol) {
        return "Bearer " + jwtService.generarToken(id, "100" + id, rol, "Persona " + id);
    }

    @Test
    void sinTokenNoSePuedeUsarLaApi() throws Exception {
        mvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void tokenFalsificadoEsRechazado() throws Exception {
        String otro = new JwtService("otra-clave-distinta-con-mas-de-32-caracteres", "anunciadores", 60000)
                .generarToken(1, "1001", AuthenticatedUser.ROL_ADMIN, "Falso");
        mvc.perform(get("/api/users").header("Authorization", "Bearer " + otro))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void cualquierUsuarioConSesionPuedeVerUsuarios() throws Exception {
        when(personaService.findAllUsers()).thenReturn(Collections.emptyList());
        mvc.perform(get("/api/users").header("Authorization", token(5, AuthenticatedUser.ROL_USUARIO)))
                .andExpect(status().isOk());
    }

    @Test
    void soloAdminPuedeCambiarRoles() throws Exception {
        mvc.perform(put("/api/personas/9/rol").param("idRol", "1")
                        .header("Authorization", token(5, AuthenticatedUser.ROL_SERVIDOR)))
                .andExpect(status().isForbidden());
        verify(personaService, never()).findUsuariosRol(anyInt(), anyInt());

        mvc.perform(put("/api/personas/9/rol").param("idRol", "1")
                        .header("Authorization", token(1, AuthenticatedUser.ROL_ADMIN)))
                .andExpect(status().isOk());
        verify(personaService).findUsuariosRol(9, 1);
    }

    @Test
    void nadieVeNotificacionesDeOtraPersona() throws Exception {
        mvc.perform(get("/api/notificaciones/7").header("Authorization", token(8, AuthenticatedUser.ROL_USUARIO)))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/notificaciones/7").header("Authorization", token(7, AuthenticatedUser.ROL_USUARIO)))
                .andExpect(status().isOk());
        mvc.perform(get("/api/notificaciones/7").header("Authorization", token(1, AuthenticatedUser.ROL_ADMIN)))
                .andExpect(status().isOk());
    }

    @Test
    void nadieSubeTcdANombreDeOtro() throws Exception {
        mvc.perform(get("/api/tdcbyPerson/7").header("Authorization", token(8, AuthenticatedUser.ROL_SERVIDOR)))
                .andExpect(status().isForbidden());
    }

    @Test
    void registroPublicoSiempreCreaUsuarioNormal() throws Exception {
        when(personaService.save(any(Persona.class), anyInt())).thenReturn(new Persona());
        String body = "{\"nombre\":\"Ana\",\"apellido\":\"Paz\",\"documento\":123,\"rol\":1}";

        mvc.perform(post("/api/personas/register").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());
        verify(personaService).save(any(Persona.class), eq(Rol.ID_USUARIO));
    }

    @Test
    void adminSiPuedeElegirRolAlRegistrar() throws Exception {
        when(personaService.save(any(Persona.class), anyInt())).thenReturn(new Persona());
        String body = "{\"nombre\":\"Ana\",\"apellido\":\"Paz\",\"documento\":124,\"rol\":2}";

        mvc.perform(post("/api/personas/register").contentType(MediaType.APPLICATION_JSON).content(body)
                        .header("Authorization", token(1, AuthenticatedUser.ROL_ADMIN)))
                .andExpect(status().isCreated());
        verify(personaService).save(any(Persona.class), eq(Rol.ID_USER));
    }

    @Test
    void registroValidaCamposObligatorios() throws Exception {
        mvc.perform(post("/api/personas/register").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"apellido\":\"Paz\",\"documento\":125}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El nombre es obligatorio"));
    }

    @Test
    void cambioDeContrasenaPorCedulaSigueSiendoPublico() throws Exception {
        mvc.perform(post("/api/updatePassword").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"documento\":123,\"password\":\"nueva123\"}"))
                .andExpect(status().is(org.hamcrest.Matchers.not(401)))
                .andExpect(status().is(org.hamcrest.Matchers.not(403)));
    }

    @Test
    void contactoEsPublicoPeroLeerMensajesEsSoloAdmin() throws Exception {
        mvc.perform(post("/api/contacto").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Ana\",\"email\":\"a@b.co\",\"mensaje\":\"Hola\"}")
                        .header("X-Real-IP", "10.0.0.50"))
                .andExpect(status().isCreated());
        mvc.perform(get("/api/contacto").header("Authorization", token(5, AuthenticatedUser.ROL_SERVIDOR)))
                .andExpect(status().isForbidden());
    }

    @Test
    void loginSeBloqueaTrasVariosIntentosFallidos() throws Exception {
        when(authService.login(any())).thenThrow(new LoginException("Cédula o contraseña incorrectos"));
        String body = "{\"cedula\":\"999\",\"password\":\"mala\"}";
        for (int i = 0; i < 3; i++) {
            mvc.perform(post("/api/auth/loginReact").contentType(MediaType.APPLICATION_JSON).content(body)
                            .header("X-Real-IP", "10.0.0.99"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("Cédula o contraseña incorrectos"));
        }
        mvc.perform(post("/api/auth/loginReact").contentType(MediaType.APPLICATION_JSON).content(body)
                        .header("X-Real-IP", "10.0.0.99"))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void laContrasenaNuncaSaleEnElJson() throws Exception {
        Persona persona = new Persona();
        persona.setPassword("$2a$10$hashsecreto");
        String json = objectMapper.writeValueAsString(persona);
        org.junit.jupiter.api.Assertions.assertFalse(json.contains("hashsecreto"), json);
        org.junit.jupiter.api.Assertions.assertFalse(json.contains("passwordHashVersion"), json);

        Persona leida = objectMapper.readValue("{\"documento\":1,\"password\":\"nueva\"}", Persona.class);
        org.junit.jupiter.api.Assertions.assertEquals("nueva", leida.getPassword());
    }
}
