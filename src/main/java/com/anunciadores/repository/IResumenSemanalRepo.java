package com.anunciadores.repository;

import com.anunciadores.model.ResumenSemanal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IResumenSemanalRepo extends JpaRepository<ResumenSemanal, Integer> {

    Optional<ResumenSemanal> findBySemanaInicio(Date semanaInicio);

    List<ResumenSemanal> findTop12ByOrderBySemanaInicioDesc();
}
