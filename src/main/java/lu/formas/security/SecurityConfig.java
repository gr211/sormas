package lu.formas.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import lu.formas.views.security.LoginView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    @Autowired
    private CustomLoginSuccessHandler loginSuccessHandler;

    @Value("${jwt.auth.secret}")
    private String authSecret;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/favicon.ico")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/frontend/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
        );

        http.csrf(csrf -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")));

        // Set default success URL
        http.formLogin(formLogin -> formLogin
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/", true)
        ).logout(LogoutConfigurer::permitAll);

        super.configure(http);
        setLoginView(http, LoginView.class);
        setStatelessAuthentication(http, new SecretKeySpec(Base64.getDecoder().decode(authSecret), JwsAlgorithms.HS256), "lu.sormas.exercise");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCrypt for password encoding
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new PatientDetailsService();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
