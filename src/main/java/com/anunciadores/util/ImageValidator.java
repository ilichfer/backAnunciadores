package com.anunciadores.util;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 * Valida que un archivo subido sea realmente una imagen revisando sus primeros bytes
 * (no se confia en el nombre ni en el Content-Type que manda el navegador).
 */
public final class ImageValidator {

    public static final String MENSAJE_FORMATO =
            "El archivo no es una imagen válida. Sube una foto en formato JPG, PNG, WEBP o GIF.";

    private ImageValidator() {
    }

    /** Formato de imagen detectado. */
    public enum Formato {
        JPG("jpg", "image/jpeg"),
        PNG("png", "image/png"),
        GIF("gif", "image/gif"),
        WEBP("webp", "image/webp");

        private final String extension;
        private final String contentType;

        Formato(String extension, String contentType) {
            this.extension = extension;
            this.contentType = contentType;
        }

        public String getExtension() {
            return extension;
        }

        public String getContentType() {
            return contentType;
        }
    }

    public static Formato validar(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes seleccionar una imagen");
        }
        byte[] header = new byte[12];
        int leidos;
        try (InputStream in = file.getInputStream()) {
            leidos = in.readNBytes(header, 0, header.length);
        }
        Formato formato = detectar(header, leidos);
        if (formato == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MENSAJE_FORMATO);
        }
        return formato;
    }

    static Formato detectar(byte[] b, int len) {
        if (len >= 3 && (b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8 && (b[2] & 0xFF) == 0xFF) {
            return Formato.JPG;
        }
        if (len >= 8 && (b[0] & 0xFF) == 0x89 && b[1] == 'P' && b[2] == 'N' && b[3] == 'G'
                && b[4] == 0x0D && b[5] == 0x0A && b[6] == 0x1A && b[7] == 0x0A) {
            return Formato.PNG;
        }
        if (len >= 6 && b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8'
                && (b[4] == '7' || b[4] == '9') && b[5] == 'a') {
            return Formato.GIF;
        }
        if (len >= 12 && b[0] == 'R' && b[1] == 'I' && b[2] == 'F' && b[3] == 'F'
                && b[8] == 'W' && b[9] == 'E' && b[10] == 'B' && b[11] == 'P') {
            return Formato.WEBP;
        }
        return null;
    }
}
