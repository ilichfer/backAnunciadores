package com.anunciadores.controller;

import com.anunciadores.model.Notificacion;
import com.anunciadores.repository.INotificacionRepo;
import com.anunciadores.security.CurrentUser;
import com.anunciadores.service.interfaces.INotificacionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Notificaciones de cada persona. Cada usuario solo puede ver y modificar las suyas.
 */
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private INotificacionService notificacionService;

    @Autowired
    private INotificacionRepo notificacionRepo;

    @GetMapping("/{idPersona}")
    public ResponseEntity<List<Notificacion>> getNotificaciones(@PathVariable Integer idPersona) {
        CurrentUser.requireSelfOrAdmin(idPersona);
        return ResponseEntity.ok(this.notificacionService.getNotificacionesPorPersona(idPersona));
    }

    @GetMapping("/{idPersona}/no-leidas")
    public ResponseEntity<List<Notificacion>> getNotificacionesNoLeidas(@PathVariable Integer idPersona) {
        CurrentUser.requireSelfOrAdmin(idPersona);
        return ResponseEntity.ok(this.notificacionService.getNotificacionesNoLeidas(idPersona));
    }

    @GetMapping("/{idPersona}/count")
    public ResponseEntity<Integer> getCountNoLeidas(@PathVariable Integer idPersona) {
        CurrentUser.requireSelfOrAdmin(idPersona);
        return ResponseEntity.ok(this.notificacionService.countNotificacionesNoLeidas(idPersona));
    }

    @PutMapping("/{id}/leida")
    public ResponseEntity<Notificacion> marcarLeida(@PathVariable Integer id) {
        Notificacion existente = this.notificacionRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificación no encontrada"));
        CurrentUser.requireSelfOrAdmin(existente.getIdPersona());
        return ResponseEntity.ok(this.notificacionService.marcarLeida(id));
    }

    @PutMapping("/{idPersona}/leer-todas")
    public ResponseEntity<Boolean> marcarTodasLeidas(@PathVariable Integer idPersona) {
        CurrentUser.requireSelfOrAdmin(idPersona);
        this.notificacionService.marcarTodasLeidas(idPersona);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/{idPersona}/limpiar/{dias}")
    public ResponseEntity<Boolean> limpiarNotificaciones(@PathVariable Integer idPersona, @PathVariable Integer dias) {
        CurrentUser.requireSelfOrAdmin(idPersona);
        this.notificacionService.eliminarNotificacionesAntiguas(idPersona, dias);
        return ResponseEntity.ok(true);
    }
}
