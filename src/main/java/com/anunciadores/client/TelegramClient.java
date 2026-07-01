package  com.anunciadores.client;
import com.anunciadores.client.FeignClientConfiguration;
import com.anunciadores.dto.ResponseTelegram;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(name = "telegramClient", url = "https://api.telegram.org", configuration = {FeignClientConfiguration.class})
public interface TelegramClient {
  @PostMapping(value = {"/bot{token}/sendMessage"}, consumes = {"application/json"})
  String enviarMensajeTelegram(@PathVariable("token") String paramString1, @RequestBody Map<String, Object> paramMap, @RequestParam("parse_mode") String paramString2);
  @GetMapping({"/bot{token}/getUpdates"})
  ResponseTelegram getUpdates(@PathVariable("token") String paramString);
}
