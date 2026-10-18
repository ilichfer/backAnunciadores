package com.anunciadores.controller;

import com.anunciadores.dto.ImagenMensualDto;
import com.anunciadores.model.ImagenMensual;
import com.anunciadores.service.interfaces.IImagenMensualService;
import com.anunciadores.service.interfaces.IR2UploadService;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 * Imagen del mes que se muestra en la pagina publica.
 */
@RestController
@RequestMapping("/api/imagen-mensual")
public class ImagenMensualController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImagenMensualController.class);

    @Autowired
    private IImagenMensualService imagenMensualService;

    @Autowired
    private IR2UploadService r2UploadService;

    @GetMapping("/{tipo}")
    public ResponseEntity<ImagenMensualDto> getImagenMensual(@PathVariable String tipo) {
        ImagenMensualDto dto = this.imagenMensualService.getImagenActual(tipo);
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay imagen disponible para: " + tipo);
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/upload")
    public ResponseEntity<ImagenMensualDto> uploadImagenMensual(
            @RequestParam("image") MultipartFile file,
            @RequestParam(value = "mes", required = false) Integer mes,
            @RequestParam(value = "anio", required = false) Integer anio,
            @RequestParam(value = "tipo", defaultValue = "tcd") String tipo) throws IOException {
        String imageUrl = this.r2UploadService.uploadImage(file);
        LocalDate hoy = LocalDate.now(ZoneId.of("America/Bogota"));
        int mesFinal = mes != null ? mes : hoy.getMonthValue();
        int anioFinal = anio != null ? anio : hoy.getYear();

        ImagenMensual guardada = this.imagenMensualService.guardarImagen(imageUrl, mesFinal, anioFinal, tipo);
        if (guardada == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al guardar el registro en la base de datos");
        }
        LOGGER.info("Imagen mensual guardada: tipo={}, mes={}, anio={}, url={}", tipo, mesFinal, anioFinal, imageUrl);
        return ResponseEntity.ok(new ImagenMensualDto(
                guardada.getUrl(), guardada.getMes(), guardada.getAnio(), guardada.getTipo()));
    }
}
