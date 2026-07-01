package  com.anunciadores.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
@Bean
public HttpFirewall getHttpFirewall() {
StrictHttpFirewall strictHttpFirewall = new StrictHttpFirewall();
strictHttpFirewall.setAllowSemicolon(true);
return (HttpFirewall)strictHttpFirewall;
}
protected void configure(HttpSecurity http) throws Exception {
((HttpSecurity)((HttpSecurity)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((HttpSecurity)((HttpSecurity)http
.csrf().disable())
.sessionManagement()
.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
.and())
.authorizeRequests()
.antMatchers(new String[] { "/**" })).permitAll()
.and())
.formLogin().disable())
.httpBasic().disable();
}
}
