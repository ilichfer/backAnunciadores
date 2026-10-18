package com.anunciadores.controller;

import com.anunciadores.service.ConfirmacionTurnoService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * El servidor confirma su turno o avisa que no puede (con motivo opcional).
 */
@RestController
@RequestMapping("/api/servicios")
public class ConfirmacionTurnoController {

    @Autowired
    private ConfirmacionTurnoService confirmacionService;

    @PutMapping("/{idServicio}/confirmacion")
    public ResponseEntity<Map<String, Object>> responder(@PathVariable int idServicio,
                                                         @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(confirmacionService.responder(idServicio, body.get("estado"), body.get("motivo")));
    }
}
