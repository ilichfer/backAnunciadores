package com.anunciadores.controller;

import com.anunciadores.service.PanelPastorService;
import com.anunciadores.service.ResumenSemanalService;
import com.anunciadores.util.Fechas;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Panel general y resumen semanal para el pastor (solo ADMIN).
 */
@RestController
@RequestMapping("/api/panel-pastor")
public class PanelPastorController {

    @Autowired
    private PanelPastorService panelService;

    @Autowired
    private ResumenSemanalService resumenService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> panel() {
        return ResponseEntity.ok(panelService.panel());
    }

    @GetMapping("/resumenes")
    public ResponseEntity<List<Map<String, Object>>> resumenes() {
        return ResponseEntity.ok(resumenService.ultimos());
    }

    /** Genera ya el resumen de la semana pasada (sin enviarlo), para consultarlo en el panel. */
    @PostMapping("/resumenes/generar")
    public ResponseEntity<Map<String, Object>> generar() {
        return ResponseEntity.ok(resumenService.generar(ResumenSemanalService.inicioSemanaAnterior(Fechas.hoy())));
    }
}
