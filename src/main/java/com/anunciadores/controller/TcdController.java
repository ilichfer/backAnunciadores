package com.anunciadores.controller;

import com.anunciadores.auth.dto.reporRequest;
import com.anunciadores.dto.TdcDto;
import com.anunciadores.dto.TdcReporteDto;
import com.anunciadores.security.CurrentUser;
import com.anunciadores.service.interfaces.IR2UploadService;
import com.anunciadores.service.interfaces.ITdcService;
import com.anunciadores.util.UtilDate;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 * TCD (Tiempo Con Dios): subida diaria de la imagen y reportes.
 */
@RestController
@RequestMapping("/api")
public class TcdController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TcdController.class);

    @Autowired
    private ITdcService tdcService;

    @Autowired
    private IR2UploadService r2UploadService;

    @Autowired
    private UtilDate utilDate;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file,
                                              @RequestParam("idPersona") Integer idPersona)
            throws ParseException, IOException {
        CurrentUser.requireSelfOrAdmin(idPersona);
        LOGGER.info("[UPLOAD] idPersona={} | fecha={} | archivo={} | tamaño={}KB | tipo={}",
                idPersona, utilDate.cargarfechaActualBogotaString(), file.getOriginalFilename(),
                file.getSize() / 1024, file.getContentType());

        boolean puedeSubir = tdcService.getTdcByFechaAndPersona(utilDate.cargarfechaActualBogotaDate(), idPersona);
        if (!puedeSubir) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya subiste tu TCD de hoy (" + utilDate.cargarfechaActualBogotaString() + ")");
        }
        String imageUrl = this.r2UploadService.uploadImage(file);
        this.tdcService.saveTcdImage(imageUrl, idPersona);
        return ResponseEntity.ok(imageUrl);
    }

    @PostMapping("/scheduleByDate")
    public ResponseEntity<List<TdcReporteDto>> scheduleByDate(@RequestBody reporRequest request) throws ParseException {
        if (request.getFechaInicio() == null || request.getFechaFin() == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(this.tdcService.findAllBetweenDates(
                this.utilDate.convertStringToDate(request.getFechaInicio()),
                this.utilDate.convertStringToDate(request.getFechaFin())));
    }

    @GetMapping("/tdcbyPerson/{idPersona}")
    public ResponseEntity<TdcReporteDto> tdcbyPerson(@PathVariable Integer idPersona) throws ParseException {
        CurrentUser.requireSelfOrAdmin(idPersona);
        TdcReporteDto tdc = this.tdcService.findAllBetweenDatesAndPerson(idPersona);
        if (tdc == null || tdc.getIdPersona() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay TCD registrados");
        }
        return ResponseEntity.ok(tdc);
    }

    @GetMapping("/findTcdPerson")
    public ResponseEntity<List<TdcDto>> findTcdPerson(@RequestParam Integer idPersona,
                                                      @RequestParam String fechaInicio,
                                                      @RequestParam String fechaFin) throws ParseException {
        CurrentUser.requireSelfOrAdmin(idPersona);
        List<TdcDto> lista = this.tdcService.findAllBetweenDatesByPersona(
                this.utilDate.convertStringToDate(fechaInicio), this.utilDate.convertStringToDate(fechaFin), idPersona);
        return ResponseEntity.ok(lista != null ? lista : Collections.emptyList());
    }
}
