package  com.anunciadores.service.interfaces;
import com.anunciadores.auth.dto.updateServiceRequest;
import com.anunciadores.dto.Ministry;
import java.text.ParseException;
import java.util.List;
public interface IMInisteryService {
  List<Ministry> getAllministriesWithPositions();
  void agregarPersonasAMinisterio(int paramInt1, int paramInt2);
  Ministry getMinistryWithPositions(Integer paramInteger);
  void updateService(updateServiceRequest paramupdateServiceRequest) throws ParseException;
}
