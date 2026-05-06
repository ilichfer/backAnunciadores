package  com.anunciadores.repository;

import com.anunciadores.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IRolesRepo extends JpaRepository<Rol, Integer> {

    @Query(value = "SELECT r.id, r.descripcion_rol FROM persona_rol pr JOIN rol r ON pr.id_rol = r.id WHERE pr.id_persona = :idPersona", nativeQuery = true)
    List<Rol> buscarRoles(@Param("idPersona") int idPersona);

}