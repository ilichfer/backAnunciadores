package  com.anunciadores.client;
import com.anunciadores.client.R2Client;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
@Configuration
public class R2Config
{
@Value("${cloudflare.r2.access-key}")
private String accessKey;
@Value("${cloudflare.r2.secret-key}")
private String secretKey;
@Value("${cloudflare.r2.endpoint}")
private String endpoint;
@Bean
public R2Client r2Client() {
S3Client client = (S3Client)((S3ClientBuilder)((S3ClientBuilder)((S3ClientBuilder)S3Client.builder().endpointOverride(URI.create(this.endpoint))).credentialsProvider((AwsCredentialsProvider)StaticCredentialsProvider.create((AwsCredentials)AwsBasicCredentials.create(this.accessKey, this.secretKey)))).region(Region.of("auto"))).build();
return new R2Client(client);
}
}
