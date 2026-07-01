package  com.anunciadores.service;
import com.anunciadores.dto.PagoDto;
import com.anunciadores.dto.ReportePagoDto;
import com.anunciadores.model.Pago;
import com.anunciadores.repository.IPagoRepo;
import com.anunciadores.repository.PagoRepoImpl;
import com.anunciadores.service.interfaces.IPagoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class PagoServiceImpl
implements IPagoService
{
@Autowired
private IPagoRepo PagoRepository;
@Autowired
private PagoRepoImpl pagoDao;
public List<Pago> findAll() {
return this.PagoRepository.findAll();
}
public Pago save(Pago pago) {
return (Pago)this.PagoRepository.save(pago);
}
public List<Pago> findPagosByIdCurso(int idpersona, int idcurso) {
List<Pago> ListaPago = this.pagoDao.findPagosByIdCurso(idpersona, Integer.valueOf(idcurso));
return ListaPago;
}
public Pago delete(Pago pago) {
this.PagoRepository.deleteById(Integer.valueOf(pago.getId()));
return pago;
}
public List<PagoDto> reportePagosCursos(int idCurso) {
return this.pagoDao.reportePagosCursos(idCurso);
}
public List<ReportePagoDto> reportePagos() {
return this.pagoDao.reportePagos();
}
}
