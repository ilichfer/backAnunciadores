package  com.anunciadores.service.interfaces;
import com.anunciadores.dto.PagoDto;
import com.anunciadores.dto.ReportePagoDto;
import com.anunciadores.model.Pago;
import java.util.List;
public interface IPagoService {
  List<Pago> findAll();
  Pago save(Pago paramPago);
  List<Pago> findPagosByIdCurso(int paramInt1, int paramInt2);
  Pago delete(Pago paramPago);
  List<PagoDto> reportePagosCursos(int paramInt);
  List<ReportePagoDto> reportePagos();
}
