package com.anunciadores.client;

import org.springframework.beans.factory.DisposableBean;
import software.amazon.awssdk.services.s3.S3Client;

public class R2Client implements DisposableBean {

    private final S3Client s3Client;

    public R2Client(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public S3Client getClient() {
        return this.s3Client;
    }

    /** Cierra el cliente (y su hilo de conexiones) al detener la aplicacion. */
    @Override
    public void destroy() {
        this.s3Client.close();
    }
}
