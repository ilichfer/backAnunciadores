package  com.anunciadores.service.interfaces;
import com.anunciadores.dto.ResponseTelegram;
import com.anunciadores.dto.ServicioResponseDto;
import com.anunciadores.model.Persona;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.text.ParseException;
import java.util.List;
public interface ITelegramService {
  List<Persona> getUpdatesWithContact() throws JsonMappingException, JsonProcessingException;
  List<Persona> getContactTelegram() throws JsonMappingException, JsonProcessingException;
  int getUpdatesWithOutContact() throws JsonMappingException, JsonProcessingException;
  List<ServicioResponseDto> sendNotification() throws JsonMappingException, JsonProcessingException, ParseException;
  ResponseTelegram getContact(String paramString) throws JsonMappingException, JsonProcessingException;
}
