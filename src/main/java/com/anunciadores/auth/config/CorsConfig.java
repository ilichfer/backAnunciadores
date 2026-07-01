package  com.anunciadores.auth.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
@Configuration
public class CorsConfig {
@Bean
public CorsFilter corsFilter() {
CorsConfiguration config = new CorsConfiguration();
config.addAllowedOrigin("http://localhost:3000");
config.addAllowedOrigin("http://localhost:5000");
config.addAllowedOrigin("http://localhost:5173");
config.addAllowedOrigin("https://anunciaig.com/");
config.addAllowedOrigin("https://anunciadores.netlify.app/");
config.addAllowedHeader("*");
config.addAllowedMethod("*");
config.setAllowCredentials(Boolean.valueOf(false));
UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
source.registerCorsConfiguration("/**", config);
return new CorsFilter((CorsConfigurationSource)source);
}
}
