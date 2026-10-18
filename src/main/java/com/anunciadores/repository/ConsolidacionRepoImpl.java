package com.anunciadores.repository;

import com.anunciadores.model.Persona;
import com.anunciadores.model.inscripcionConsolidacion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConsolidacionRepoImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsolidacionRepoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    public inscripcionConsolidacion listarConsolidacionByPersona(int idPersona) {
        try {
            List<?> resultado = this.entityManager
                    .createNativeQuery("select * from consolidacion c where c.id_persona = ?1",
                            inscripcionConsolidacion.class)
                    .setParameter(1, idPersona)
                    .getResultList();
            return resultado.isEmpty() ? null : (inscripcionConsolidacion) resultado.get(0);
        } catch (Exception e) {
            LOGGER.error("Error al consultar la consolidación de la persona {}", idPersona, e);
            return null;
        }
    }

    public Persona listarPersonaConsolidacion(int idPersona) {
        try {
            List<?> resultado = this.entityManager
                    .createNativeQuery("select p.* from consolidacion c "
                            + "join persona p on c.id_persona_consolidar = p.id "
                            + "where c.id_persona = ?1", Persona.class)
                    .setParameter(1, idPersona)
                    .getResultList();
            return resultado.isEmpty() ? null : (Persona) resultado.get(0);
        } catch (Exception e) {
            LOGGER.error("Error al consultar la persona en consolidación {}", idPersona, e);
            return null;
        }
    }
}
