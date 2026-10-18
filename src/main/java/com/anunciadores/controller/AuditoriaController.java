package com.anunciadores.controller;

import com.anunciadores.model.Auditoria;
import com.anunciadores.service.AuditoriaService;
import com.anunciadores.util.Fechas;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Bitacora de auditoria (solo ADMIN).
 */
@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    private static final List<String> ACCIONES = Arrays.asList(
            AuditoriaService.CAMBIO_ROL, AuditoriaService.CAMBIO_ESTADO_USUARIO, AuditoriaService.REGISTRO_PERSONA,
            AuditoriaService.CAMBIO_CONTRASENA, AuditoriaService.PROGRAMACION, AuditoriaService.COORDINADOR,
            AuditoriaService.CONFIRMACION_TURNO, AuditoriaService.MINISTERIO, AuditoriaService.NOTA,
            AuditoriaService.CURSO, AuditoriaService.CONFIGURACION, AuditoriaService.IMAGEN_MES,
            AuditoriaService.WHATSAPP);

    @Autowired
    private AuditoriaService auditoriaService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> buscar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) String accion,
            @RequestParam(required = false) String texto,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "50") int tamano) {
        LocalDate fin = hasta != null ? hasta : Fechas.hoy();
        LocalDate inicio = desde != null ? desde : fin.minusDays(30);
        Page<Auditoria> resultado = auditoriaService.buscar(Fechas.aDate(inicio), Fechas.aDate(fin.plusDays(1)),
                accion, texto, pagina, tamano);
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("registros", resultado.getContent());
        r.put("total", resultado.getTotalElements());
        r.put("pagina", resultado.getNumber());
        r.put("paginas", resultado.getTotalPages());
        return ResponseEntity.ok(r);
    }

    @GetMapping("/acciones")
    public ResponseEntity<List<String>> acciones() {
        return ResponseEntity.ok(ACCIONES);
    }
}
