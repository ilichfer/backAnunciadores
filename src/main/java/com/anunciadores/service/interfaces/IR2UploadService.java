package  com.anunciadores.service.interfaces;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
public interface IR2UploadService {
  String uploadImage(MultipartFile paramMultipartFile) throws IOException;
}
