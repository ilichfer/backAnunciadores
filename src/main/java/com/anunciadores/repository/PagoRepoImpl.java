  package  com.anunciadores.repository;
  import com.anunciadores.dto.PagoDto;
  import com.anunciadores.dto.ReportePagoDto;
  import com.anunciadores.model.Pago;
  import java.util.ArrayList;
  import java.util.List;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.jdbc.core.JdbcTemplate;
  import org.springframework.jdbc.core.ResultSetExtractor;
  import org.springframework.stereotype.Service;
  @Service
  public class PagoRepoImpl
  {
    @Autowired
    JdbcTemplate jdbcTemplate;
    public List<Pago> findPagosByIdCurso(int idpersona, Integer idcurso) {
StringBuilder sql = new StringBuilder();
List<Pago> pagoList = new ArrayList<>();
      try {
sql.append("select p.* from pago p \njoin curso c on p.id_curso = c.id \nwhere c.id =" + idcurso + " and p.idpersona = " + idpersona);
this.jdbcTemplate.query(sql.toString(), (ResultSetExtractor)new Object());
      }
catch (Exception e) {
e.printStackTrace();
      } 
return pagoList;
    }
    public List<PagoDto> reportePagosCursos(int idCurso) {
StringBuilder sql = new StringBuilder();
List<PagoDto> pagoList = new ArrayList<>();
      try {
sql.append("select SUM(pa.valor) as suma, pa.id_curso, pa.idpersona, c.valortotal from curso c  join pago pa on c.id = pa.id_curso  join persona pe on pa.idpersona = pe.id  where pa.id_curso = " + idCurso + " GROUP by pa.idpersona ");
this.jdbcTemplate.query(sql.toString(), (ResultSetExtractor)new Object());
      }
catch (Exception e) {
e.printStackTrace();
      } 
return pagoList;
    }
    public List<ReportePagoDto> reportePagos() {
StringBuilder sql = new StringBuilder();
List<ReportePagoDto> pagoList = new ArrayList<>();
      try {
sql.append("select c.id ,c.nombrecurso ,c.valortotal ,pe.nombre, (select sum(pa.valor) from pago pa where pa.idpersona = pe.id and pa.id_curso = c.id) as pagos, case  when (select sum(pa.valor) from pago pa where pa.idpersona = pe.id and pa.id_curso = c.id) is null then c.valortotal   when (select sum(pa.valor) from pago pa where pa.idpersona = pe.id and pa.id_curso = c.id) is not null  then valortotal-(select sum(pa.valor) from pago pa where pa.idpersona = pe.id and pa.id_curso = c.id) else Null end adeuda from curso c  join inscripciones i on c.id  = i.id_curso  join persona pe on i.id_persona = pe.id  order by c.nombrecurso asc ");
this.jdbcTemplate.query(sql.toString(), (ResultSetExtractor)new Object());
      }
catch (Exception e) {
e.printStackTrace();
      } 
return pagoList;
    }
  }
