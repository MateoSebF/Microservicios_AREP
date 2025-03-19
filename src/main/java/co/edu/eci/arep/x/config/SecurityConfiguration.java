package co.edu.eci.arep.x.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configures AWS Cognito as an OAuth 2.0 authorizer with Spring Security.
 * This setup ensures proper authentication handling and user session management.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CognitoLogoutHandler cognitoLogoutHandler = new CognitoLogoutHandler();

        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF if not needed
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/index.html", "/styles.css", "/script.js").permitAll() // Public routes
                .anyRequest().authenticated() // Require authentication for all other requests
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo.oidcUserService(new OidcUserService()))
                .defaultSuccessUrl("/home.html", true) // Redirect after login
            )
            .logout(logout -> logout
                .logoutSuccessHandler(cognitoLogoutHandler) // Handle logout with Cognito
            );

        return http.build();
    }
}