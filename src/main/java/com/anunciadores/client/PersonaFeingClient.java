package  com.anunciadores.client;
import com.anunciadores.dto.PersonaDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(name = "persona", url = "http://44.211.227.165:8080")
public interface PersonaFeingClient {
  @GetMapping({"/consutarEmail?email="})
  PersonaDto buscarPersona(@RequestParam String paramString);
  @GetMapping({"/consutarDoc?doc="})
  PersonaDto consutarDoc(@RequestParam String paramString);
}
