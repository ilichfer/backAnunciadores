package com.anunciadores.controller;

import com.anunciadores.dto.ContactoDto;
import com.anunciadores.dto.ContactoRequestDto;
import com.anunciadores.security.RateLimiter;
import com.anunciadores.service.interfaces.IContactoService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mensajes del formulario de contacto. Enviar es publico; leerlos es solo para ADMIN.
 */
@RestController
@RequestMapping("/api/contacto")
public class ContactoController {

    @Autowired
    private IContactoService contactoService;

    @Autowired
    private RateLimiter rateLimiter;

    @PostMapping
    public ResponseEntity<?> guardarContacto(@Valid @RequestBody ContactoRequestDto request, HttpServletRequest http) {
        if (!rateLimiter.tryAcquire("contacto:" + RateLimiter.clientIp(http), rateLimiter.getMaxAttempts())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Has enviado varios mensajes seguidos. Intenta de nuevo más tarde.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.contactoService.guardar(request));
    }

    @GetMapping
    public ResponseEntity<List<ContactoDto>> listarContactos(
            @RequestParam(value = "soloNoLeidos", defaultValue = "false") boolean soloNoLeidos) {
        return ResponseEntity.ok(soloNoLeidos ? this.contactoService.listarNoLeidos() : this.contactoService.listarTodos());
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<ContactoDto> marcarContactoLeido(@PathVariable Integer id) {
        return ResponseEntity.ok(this.contactoService.marcarLeido(id));
    }

    @GetMapping("/no-leidos/count")
    public ResponseEntity<Long> contarContactosNoLeidos() {
        return ResponseEntity.ok(this.contactoService.contarNoLeidos());
    }

    /** El servicio usa IllegalArgumentException para validaciones con mensaje para el usuario. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
