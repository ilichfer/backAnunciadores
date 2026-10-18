package com.anunciadores.service;

import com.anunciadores.client.R2Client;
import com.anunciadores.service.interfaces.IR2UploadService;
import com.anunciadores.util.ImageValidator;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class R2UploadService implements IR2UploadService {

    private final R2Client r2Client;
    private final String bucket;
    private final String publicUrl;

    public R2UploadService(R2Client r2Client,
                           @Value("${cloudflare.r2.bucket}") String bucket,
                           @Value("${cloudflare.r2.public-url}") String publicUrl) {
        this.r2Client = r2Client;
        this.bucket = bucket;
        this.publicUrl = publicUrl;
    }

    /**
     * Sube una imagen al bucket. El tipo se detecta por el contenido real del archivo, de modo que
     * no se pueden subir HTML, SVG ni otros archivos disfrazados de imagen.
     */
    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        ImageValidator.Formato formato = ImageValidator.validar(file);
        String fileName = UUID.randomUUID() + "." + formato.getExtension();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(this.bucket)
                .key(fileName)
                .contentType(formato.getContentType())
                .build();

        this.r2Client.getClient().putObject(request,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return this.publicUrl + "/" + fileName;
    }
}
