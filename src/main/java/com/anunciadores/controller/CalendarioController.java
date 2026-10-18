package com.anunciadores.controller;

import com.anunciadores.model.Persona;
import com.anunciadores.security.AuthenticatedUser;
import com.anunciadores.security.CurrentUser;
import com.anunciadores.service.CalendarioService;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Calendario de turnos y su exportacion a Google Calendar (.ics).
 */
@RestController
@RequestMapping("/api/calendario")
public class CalendarioController {

    private static final String URL_PUBLICA = "https://anunciaig.com/api/calendario/ics/";

    @Autowired
    private CalendarioService calendarioService;

    /** Mis turnos, o los de toda la iglesia (todos=true) para administradores y servidores. */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> eventos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(defaultValue = "false") boolean todos) {
        AuthenticatedUser usuario = CurrentUser.get();
        boolean puedeVerTodos = usuario.isAdmin() || AuthenticatedUser.ROL_SERVIDOR.equals(usuario.getRol());
        Integer filtroPersona = todos && puedeVerTodos ? null : usuario.getId();
        return ResponseEntity.ok(calendarioService.eventos(desde, hasta, filtroPersona, usuario.getId(), usuario.isAdmin()));
    }

    /** Enlace privado para suscribir "Mis turnos" en Google Calendar (se actualiza solo). */
    @GetMapping("/suscripcion")
    public ResponseEntity<Map<String, String>> suscripcion() {
        return ResponseEntity.ok(enlaces(calendarioService.tokenDe(CurrentUser.get().getId())));
    }

    @PostMapping("/suscripcion/regenerar")
    public ResponseEntity<Map<String, String>> regenerar() {
        return ResponseEntity.ok(enlaces(calendarioService.regenerarToken(CurrentUser.get().getId())));
    }

    /** Publico pero protegido por el token secreto: lo consulta Google Calendar. */
    @GetMapping("/ics/{archivo}")
    public ResponseEntity<byte[]> ics(@PathVariable String archivo) {
        String token = archivo.endsWith(".ics") ? archivo.substring(0, archivo.length() - 4) : archivo;
        Persona persona = calendarioService.personaPorToken(token).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Calendario no encontrado"));
        byte[] cuerpo = calendarioService.icsPersona(persona).getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(new MediaType("text", "calendar", StandardCharsets.UTF_8))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"mis-turnos.ics\"")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(cuerpo);
    }

    private static Map<String, String> enlaces(String token) {
        String url = URL_PUBLICA + token + ".ics";
        String webcal = url.replaceFirst("^https://", "webcal://");
        Map<String, String> r = new LinkedHashMap<>();
        r.put("url", url);
        r.put("webcal", webcal);
        r.put("google", "https://calendar.google.com/calendar/r?cid=" + webcal);
        return r;
    }
}
