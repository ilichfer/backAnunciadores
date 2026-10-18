package com.anunciadores.repository;

import com.anunciadores.model.PushSuscripcion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface IPushSuscripcionRepo extends JpaRepository<PushSuscripcion, Integer> {

    List<PushSuscripcion> findByIdPersona(Integer idPersona);

    Optional<PushSuscripcion> findByEndpoint(String endpoint);

    @Transactional
    void deleteByEndpoint(String endpoint);
}
