package  com.anunciadores.service.interfaces;
import com.anunciadores.dto.CoordinadorDTO;
import java.util.Date;
import java.util.List;
public interface ICordinadorService {
  List<CoordinadorDTO> findAllBetweenDates(Date paramDate1, Date paramDate2);
}
