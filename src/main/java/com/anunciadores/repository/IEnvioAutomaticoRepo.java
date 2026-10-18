package com.anunciadores.repository;

import com.anunciadores.model.EnvioAutomatico;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEnvioAutomaticoRepo extends JpaRepository<EnvioAutomatico, Long> {

    boolean existsByTipoAndIdPersonaAndFecha(String tipo, Integer idPersona, Date fecha);
}
