package com.anunciadores.repository;

import com.anunciadores.model.Auditoria;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IAuditoriaRepo extends JpaRepository<Auditoria, Long> {

    @Query("select a from Auditoria a where a.fecha >= :desde and a.fecha < :hasta "
            + "and (:accion is null or a.accion = :accion) "
            + "and (:texto is null or lower(a.usuario) like :texto or lower(a.descripcion) like :texto) "
            + "order by a.fecha desc")
    Page<Auditoria> buscar(@Param("desde") Date desde, @Param("hasta") Date hasta,
                           @Param("accion") String accion, @Param("texto") String texto, Pageable pageable);
}
