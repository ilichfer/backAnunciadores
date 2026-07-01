package  com.anunciadores.service.interfaces;
import com.anunciadores.model.Sugerencia;
public interface ISendNotificationTelegramService {
  String sendNotificationTelegram(Sugerencia paramSugerencia, Integer paramInteger);
}
