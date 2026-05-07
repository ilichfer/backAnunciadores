/*    */ package  com.anunciadores.service;
/*    */ 
/*    */ import com.anunciadores.client.R2Client;
/*    */ import com.anunciadores.service.interfaces.IR2UploadService;
/*    */ import java.io.IOException;
/*    */ import java.util.UUID;
/*    */ import org.springframework.beans.factory.annotation.Value;
/*    */ import org.springframework.stereotype.Service;
/*    */ import org.springframework.web.multipart.MultipartFile;
/*    */ import software.amazon.awssdk.core.sync.RequestBody;
/*    */ import software.amazon.awssdk.services.s3.model.PutObjectRequest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class R2UploadService
/*    */   implements IR2UploadService
/*    */ {
/*    */   private final R2Client r2Client;
/*    */   @Value("${cloudflare.r2.bucket}")
/*    */   private String bucket;
/*    */   @Value("${cloudflare.r2.public-url}")
/*    */   private String publicUrl;
/*    */   
/*    */   public R2UploadService(R2Client r2Client, @Value("${cloudflare.r2.bucket}") String bucket, @Value("${cloudflare.r2.public-url}") String publicUrl) {
/* 29 */     this.r2Client = r2Client;
/* 30 */     this.bucket = bucket;
/* 31 */     this.publicUrl = publicUrl;
/*    */   }
/*    */   
/*    */ public String uploadImage(MultipartFile file) throws IOException {
    // 1. Obtener la extensión original (ej: "jpg", "png", "webp")
    String originalName = file.getOriginalFilename();
    String extension = "png"; // Por defecto por si no se encuentra

    if (originalName != null && originalName.contains(".")) {
        extension = originalName.substring(originalName.lastIndexOf(".") + 1);
    }

    // 2. Generar el nombre usando UUID + la extensión real
    String fileName = UUID.randomUUID().toString() + "." + extension;

    // 3. Configurar la petición
    // Usamos file.getContentType() para que coincida con el archivo real
    PutObjectRequest request = PutObjectRequest.builder()
            .bucket(this.bucket)
            .key(fileName)
            .contentType(file.getContentType())
            .build();

    // 4. Subir al R2
    this.r2Client.getClient().putObject(request,
            RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

    // 5. Retornar la URL bien formada
    return this.publicUrl + "/" + fileName;
}
/*    */ }


/* Location:              C:\Users\Asus VivoBook\.m2\repository\com\anunciadores\anunciadores\0.0.1-SNAPSHOT\ROOT.war!\WEB-INF\classes\com\anunciadores\service\R2UploadService.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */