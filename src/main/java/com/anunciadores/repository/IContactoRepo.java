package com.anunciadores.repository;
import com.anunciadores.model.Contacto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IContactoRepo extends JpaRepository<Contacto, Integer> {
    List<Contacto> findAllByOrderByFechaCreacionDesc();
    List<Contacto> findByLeidoFalseOrderByFechaCreacionDesc();
    long countByLeidoFalse();
}
