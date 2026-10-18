package com.anunciadores.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.anunciadores.controller.AuditoriaController;
import com.anunciadores.controller.CalendarioController;
import com.anunciadores.controller.ConfirmacionTurnoController;
import com.anunciadores.controller.PanelPastorController;
import com.anunciadores.controller.PushController;
import com.anunciadores.controller.WhatsAppController;
import com.anunciadores.model.Persona;
import com.anunciadores.service.AuditoriaService;
import com.anunciadores.service.CalendarioService;
import com.anunciadores.service.ConfirmacionTurnoService;
import com.anunciadores.service.JwtService;
import com.anunciadores.service.NotificacionPushService;
import com.anunciadores.service.PanelPastorService;
import com.anunciadores.service.ResumenSemanalService;
import com.anunciadores.service.WhatsAppService;
import com.anunciadores.service.interfaces.IConfiguracionService;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Reglas de acceso de las funcionalidades pastorales.
 */
@WebMvcTest(controllers = {AuditoriaController.class, CalendarioController.class, ConfirmacionTurnoController.class,
        PanelPastorController.class, PushController.class, WhatsAppController.class})
@Import({SecurityConfig.class, RateLimiter.class, JwtService.class})
@TestPropertySource(properties = {
        "security.jwt.secret=clave-de-pruebas-con-mas-de-32-caracteres-123",
        "security.jwt.issuer=anunciadores",
        "security.jwt.ttlMillis=60000",
        "security.cors.allowed-origins=http://localhost:3000"
})
class FuncionalidadesPastoralesTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JwtService jwtService;

    @MockBean
    private AuditoriaService auditoriaService;
    @MockBean
    private CalendarioService calendarioService;
    @MockBean
    private ConfirmacionTurnoService confirmacionService;
    @MockBean
    private PanelPastorService panelService;
    @MockBean
    private ResumenSemanalService resumenService;
    @MockBean
    private NotificacionPushService pushService;
    @MockBean
    private WhatsAppService whatsAppService;
    @MockBean
    private IConfiguracionService configuracionService;

    private String token(int id, String rol) {
        return "Bearer " + jwtService.generarToken(id, "100" + id, rol, "Persona " + id);
    }

    @Test
    void bitacoraYPanelSoloParaAdmin() throws Exception {
        String servidor = token(5, AuthenticatedUser.ROL_SERVIDOR);
        mvc.perform(get("/api/auditoria").header("Authorization", servidor)).andExpect(status().isForbidden());
        mvc.perform(get("/api/panel-pastor").header("Authorization", servidor)).andExpect(status().isForbidden());
        mvc.perform(get("/api/whatsapp/estado").header("Authorization", servidor)).andExpect(status().isForbidden());

        when(auditoriaService.buscar(any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(Page.empty());
        when(panelService.panel()).thenReturn(Collections.emptyMap());
        String admin = token(1, AuthenticatedUser.ROL_ADMIN);
        mvc.perform(get("/api/auditoria").header("Authorization", admin)).andExpect(status().isOk());
        mvc.perform(get("/api/panel-pastor").header("Authorization", admin)).andExpect(status().isOk());
    }

    @Test
    void usuarioNormalSoloVeSuPropioCalendario() throws Exception {
        when(calendarioService.eventos(any(), any(), any(), any(), anyBoolean())).thenReturn(Collections.emptyList());
        mvc.perform(get("/api/calendario").param("desde", "2026-10-01").param("hasta", "2026-10-31")
                        .param("todos", "true").header("Authorization", token(9, AuthenticatedUser.ROL_USUARIO)))
                .andExpect(status().isOk());
        verify(calendarioService).eventos(any(), any(), eq(9), eq(9), eq(false));
    }

    @Test
    void servidorPuedeVerCalendarioDeTodaLaIglesia() throws Exception {
        when(calendarioService.eventos(any(), any(), any(), any(), anyBoolean())).thenReturn(Collections.emptyList());
        mvc.perform(get("/api/calendario").param("desde", "2026-10-01").param("hasta", "2026-10-31")
                        .param("todos", "true").header("Authorization", token(4, AuthenticatedUser.ROL_SERVIDOR)))
                .andExpect(status().isOk());
        verify(calendarioService).eventos(any(), any(), isNull(), eq(4), eq(false));
    }

    @Test
    void calendarioIcsEsPublicoConTokenValidoYNoExisteConTokenFalso() throws Exception {
        when(calendarioService.personaPorToken("token-falso")).thenReturn(Optional.empty());
        mvc.perform(get("/api/calendario/ics/token-falso.ics")).andExpect(status().isNotFound());

        Persona persona = new Persona();
        persona.setId(3);
        when(calendarioService.personaPorToken("tokenvalidodepruebaconmasde30caracteres")).thenReturn(Optional.of(persona));
        when(calendarioService.icsPersona(persona)).thenReturn("BEGIN:VCALENDAR\r\nEND:VCALENDAR\r\n");
        mvc.perform(get("/api/calendario/ics/tokenvalidodepruebaconmasde30caracteres.ics"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/calendar"));
    }

    @Test
    void confirmacionRequiereSesion() throws Exception {
        mvc.perform(put("/api/servicios/10/confirmacion").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\":\"CONFIRMADO\"}"))
                .andExpect(status().isUnauthorized());
        verify(confirmacionService, never()).responder(anyInt(), anyString(), any());

        when(confirmacionService.responder(eq(10), eq("NO_PUEDE"), eq("viaje"))).thenReturn(Collections.emptyMap());
        mvc.perform(put("/api/servicios/10/confirmacion").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\":\"NO_PUEDE\",\"motivo\":\"viaje\"}")
                        .header("Authorization", token(7, AuthenticatedUser.ROL_SERVIDOR)))
                .andExpect(status().isOk());
    }

    @Test
    void suscripcionPushValidaElEndpoint() throws Exception {
        String t = token(7, AuthenticatedUser.ROL_USUARIO);
        mvc.perform(post("/api/push/suscripcion").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"endpoint\":\"http://inseguro\",\"keys\":{\"p256dh\":\"a\",\"auth\":\"b\"}}")
                        .header("Authorization", t))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/push/suscripcion").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"endpoint\":\"https://fcm.googleapis.com/x\",\"keys\":{\"p256dh\":\"a\",\"auth\":\"b\"}}")
                        .header("Authorization", t))
                .andExpect(status().isNoContent());
        verify(pushService).suscribir(eq(7), eq("https://fcm.googleapis.com/x"), eq("a"), eq("b"), any());
    }
}
