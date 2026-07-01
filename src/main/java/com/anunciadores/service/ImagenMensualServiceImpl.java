package com.anunciadores.service;
import com.anunciadores.dto.ImagenMensualDto;
import com.anunciadores.model.ImagenMensual;
import com.anunciadores.repository.IImagenMensualRepo;
import com.anunciadores.service.interfaces.IImagenMensualService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class ImagenMensualServiceImpl implements IImagenMensualService {
    private final Logger log = LoggerFactory.getLogger(ImagenMensualServiceImpl.class);
    @Autowired
    private IImagenMensualRepo imagenMensualRepo;
    @Override
    public ImagenMensualDto getImagenActual(String tipo) {
        try {
            ImagenMensual result = imagenMensualRepo
                .findTopByTipoAndActivoOrderByAnioDescMesDesc(tipo, true)
                .orElse(null);
            if (result == null) {
                log.warn("No se encontró imagen activa para tipo: {}", tipo);
                return null;
            }
            return new ImagenMensualDto(result.getUrl(), result.getMes(), result.getAnio(), result.getTipo());
        } catch (Exception e) {
            log.error("Error al obtener imagen actual para tipo: {}", tipo, e);
            return null;
        }
    }
    @Override
    @Transactional
    public ImagenMensual guardarImagen(String url, Integer mes, Integer anio, String tipo) {
        try {
            Optional<ImagenMensual> existente = imagenMensualRepo
                .findByTipoAndMesAndAnio(tipo, mes, anio);
            ImagenMensual guardada;
            if (existente.isPresent()) {
                guardada = existente.get();
                guardada.setUrl(url);
                guardada.setActivo(true);
                guardada = imagenMensualRepo.save(guardada);
            } else {
                ImagenMensual nueva = new ImagenMensual();
                nueva.setUrl(url);
                nueva.setMes(mes);
                nueva.setAnio(anio);
                nueva.setTipo(tipo);
                nueva.setActivo(true);
                guardada = imagenMensualRepo.save(nueva);
            }
            Optional<ImagenMensual> activoActual = imagenMensualRepo
                .findTopByTipoAndActivoOrderByAnioDescMesDesc(tipo, true);
            if (activoActual.isPresent() && !activoActual.get().getId().equals(guardada.getId())) {
                ImagenMensual old = activoActual.get();
                old.setActivo(false);
                imagenMensualRepo.save(old);
            }
            return guardada;
        } catch (Exception e) {
            log.error("Error al guardar imagen mensual", e);
            return null;
        }
    }
}
