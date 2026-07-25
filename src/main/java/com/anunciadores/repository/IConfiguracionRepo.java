package com.anunciadores.repository;
import com.anunciadores.model.Configuracion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IConfiguracionRepo extends JpaRepository<Configuracion, Integer> {
    Optional<Configuracion> findByClave(String clave);
    List<Configuracion> findByClaveIn(List<String> claves);
}
