package com.anunciadores.controller;

import com.anunciadores.service.AuditoriaService;
import com.anunciadores.service.interfaces.IConfiguracionService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Configuracion general (pesos de las notas de los cursos).
 */
@RestController
@RequestMapping("/api/configuracion")
public class ConfiguracionController {

    @Autowired
    private IConfiguracionService configuracionService;

    @Autowired
    private AuditoriaService auditoria;

    @GetMapping("/pesos-nota")
    public ResponseEntity<?> getPesosNota() {
        return ResponseEntity.ok(configuracionService.findPesosNota());
    }

    @PutMapping("/pesos-nota")
    public ResponseEntity<String> savePesosNota(@RequestBody Map<String, Double> pesos) {
        double total = 0;
        for (Double v : pesos.values()) {
            total += v != null ? v : 0;
        }
        if (Math.abs(total - 100) > 0.01) {
            return ResponseEntity.badRequest().body("La suma de los pesos debe ser 100%. Actual: " + total);
        }
        configuracionService.savePesosNota(pesos);
        auditoria.registrar(AuditoriaService.CONFIGURACION, "configuracion", null, "Pesos de nota: " + pesos);
        return ResponseEntity.ok("Pesos de nota actualizados correctamente");
    }
}
