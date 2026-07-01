package  com.anunciadores.dto;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;
public class CustomMultipartFile
implements MultipartFile {
public CustomMultipartFile(byte[] imgContent) {
this.imgContent = imgContent;
}
private final byte[] imgContent;
public String getName() {
return null;
}
public String getOriginalFilename() {
return null;
}
public String getContentType() {
return null;
}
public boolean isEmpty() {
return (this.imgContent == null || this.imgContent.length == 0);
}
public long getSize() {
return this.imgContent.length;
}
public byte[] getBytes() throws IOException {
return this.imgContent;
}
public InputStream getInputStream() throws IOException {
return new ByteArrayInputStream(this.imgContent);
}
public void transferTo(File dest) throws IOException, IllegalStateException {
(new FileOutputStream(dest)).write(this.imgContent);
}
}
