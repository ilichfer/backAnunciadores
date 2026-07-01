package  com.anunciadores.service.interfaces;
import com.anunciadores.dto.BibliaDto;
import com.anunciadores.dto.CapitulosDto;
import com.anunciadores.dto.LibrosDto;
import com.anunciadores.dto.VersiculoDto;
import com.anunciadores.dto.VersiculoResponseDto;
import com.anunciadores.dto.VersiculoSaveDto;
import com.anunciadores.dto.VersiculosDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.text.ParseException;
public interface IBibliaService {
  BibliaDto findBible(String paramString) throws JsonMappingException, JsonProcessingException;
  LibrosDto findBook(String paramString) throws JsonMappingException, JsonProcessingException;
  CapitulosDto findChapters(String paramString1, String paramString2) throws JsonMappingException, JsonProcessingException;
  VersiculosDto findIdVerses(String paramString1, String paramString2) throws JsonMappingException, JsonProcessingException;
  VersiculoResponseDto findVerse(String paramString1, String paramString2) throws JsonMappingException, JsonProcessingException;
  VersiculoDto findVerseDay() throws JsonMappingException, JsonProcessingException;
  LibrosDto findAllBooks() throws JsonMappingException, JsonProcessingException;
  VersiculoSaveDto saveVerseWeek(VersiculoSaveDto paramVersiculoSaveDto) throws JsonProcessingException, ParseException;
  VersiculoSaveDto buscarVersiculoSemanal() throws ParseException;
}
