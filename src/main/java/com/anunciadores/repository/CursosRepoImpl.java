package com.anunciadores.repository;

import com.anunciadores.model.Curso;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

@Service
public class CursosRepoImpl {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<Curso> cursosByIdPersona(Integer idPersona) {
        return this.entityManager
                .createNativeQuery("select c.* from persona p "
                        + "join inscripciones ins on p.id = ins.id_persona "
                        + "join curso c on ins.id_curso = c.id "
                        + "where p.id = ?1", Curso.class)
                .setParameter(1, idPersona)
                .getResultList();
    }
}
