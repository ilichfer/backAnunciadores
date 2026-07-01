package  com.anunciadores.service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.stereotype.Service;
@Service
public class JwtService
{
private static final String SECRET = "adcIglesiaDigitalSecretKey2026!!";
private static final long EXPIRATION_MS = 28800000L;
public String generarToken(String cedula, String rol, String nombre) {
return Jwts.builder()
.setSubject(cedula)
.claim("rol", rol)
.claim("nombre", nombre)
.setIssuedAt(new Date())
.setExpiration(new Date(System.currentTimeMillis() + 28800000L))
.signWith(SignatureAlgorithm.HS256, "adcIglesiaDigitalSecretKey2026!!".getBytes())
.compact();
}
public String getCedulaDesdeToken(String token) {
return getClaims(token).getSubject();
}
public boolean esTokenValido(String token) {
try {
Claims claims = getClaims(token); return 
!claims.getExpiration().before(new Date());
} catch (JwtException|IllegalArgumentException e) {
return false;
} 
}
private Claims getClaims(String token) {
return (Claims)Jwts.parser()
.setSigningKey("adcIglesiaDigitalSecretKey2026!!".getBytes())
.parseClaimsJws(token)
.getBody();
}
}
