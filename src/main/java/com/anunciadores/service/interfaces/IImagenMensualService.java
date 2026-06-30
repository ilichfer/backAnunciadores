package com.anunciadores.service.interfaces;

import com.anunciadores.dto.ImagenMensualDto;
import com.anunciadores.model.ImagenMensual;

public interface IImagenMensualService {

    ImagenMensualDto getImagenActual(String tipo);

    ImagenMensual guardarImagen(String url, Integer mes, Integer anio, String tipo);
}
