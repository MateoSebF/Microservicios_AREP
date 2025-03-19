package co.edu.eci.arep.x.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Class to configure AWS Cognito as an OAuth 2.0 authorizer with Spring
 * Security.
 * In this configuration, we specify our OAuth Client.
 * We also declare that all requests must come from an authenticated user.
 * Finally, we configure our logout handler.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CognitoLogoutHandler cognitoLogoutHandler = new CognitoLogoutHandler();

        http
                .csrf(AbstractHttpConfigurer::disable) // Deshabilita CSRF si no es necesario
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/index.html", "/styles.css", "/script.js").permitAll() // Rutas públicas
                        .anyRequest().authenticated() // Todo lo demás requiere autenticación
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/home.html", true) // Redirigir a home después del login
                )
                .logout(logout -> logout
                        .logoutSuccessHandler(cognitoLogoutHandler) // Manejo de logout con Cognito
                );

        return http.build();
    }
}