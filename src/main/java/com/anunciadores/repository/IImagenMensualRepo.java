package com.anunciadores.repository;
import com.anunciadores.model.ImagenMensual;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IImagenMensualRepo extends JpaRepository<ImagenMensual, Integer> {
    Optional<ImagenMensual> findTopByTipoAndActivoOrderByAnioDescMesDesc(String tipo, Boolean activo);
    Optional<ImagenMensual> findByTipoAndMesAndAnio(String tipo, Integer mes, Integer anio);
}
