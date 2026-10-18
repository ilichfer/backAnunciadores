package com.anunciadores.repository;

import com.anunciadores.dto.PagoDto;
import com.anunciadores.dto.ReportePagoDto;
import com.anunciadores.model.Pago;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PagoRepoImpl {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<Pago> findPagosByIdCurso(int idpersona, Integer idcurso) {
        return this.entityManager
                .createNativeQuery("select p.* from pago p "
                        + "join curso c on p.id_curso = c.id "
                        + "where c.id = ?1 and p.idpersona = ?2", Pago.class)
                .setParameter(1, idcurso)
                .setParameter(2, idpersona)
                .getResultList();
    }

    public List<PagoDto> reportePagosCursos(int idCurso) {
        String sql = "select sum(pa.valor) as suma, pa.id_curso, pa.idpersona, c.valortotal "
                + "from curso c "
                + "join pago pa on c.id = pa.id_curso "
                + "join persona pe on pa.idpersona = pe.id "
                + "where pa.id_curso = ? "
                + "group by pa.idpersona";
        return this.jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PagoDto.class), idCurso);
    }

    public List<ReportePagoDto> reportePagos() {
        String pagos = "(select sum(pa.valor) from pago pa where pa.idpersona = pe.id and pa.id_curso = c.id)";
        String sql = "select c.id, c.nombrecurso, c.valortotal, pe.nombre, " + pagos + " as pagos, "
                + "case when " + pagos + " is null then c.valortotal "
                + "else c.valortotal - " + pagos + " end as adeuda "
                + "from curso c "
                + "join inscripciones i on c.id = i.id_curso "
                + "join persona pe on i.id_persona = pe.id "
                + "order by c.nombrecurso asc";
        return this.jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ReportePagoDto.class));
    }
}
