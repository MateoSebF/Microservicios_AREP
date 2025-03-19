package co.edu.eci.arep.x.controller;

import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@RestController
public class AuthController {

    private static final String COGNITO_LOGOUT_URL = "https://cognito-idp.us-east-1.amazonaws.com/us-east-1_3LaGh3i29/logout";
    private static final String CLIENT_ID = "57a363flss9rbedmjc6g16gd9n";
    private static final String LOGOUT_REDIRECT_URI = "https://backendeci.duckdns.org:8080/index.html";

    /**
     * Redirige a home.html si el usuario ya está autenticado, de lo contrario, lo envía a index.html.
     */
    @GetMapping("/")
    public void redirectIfAuthenticated(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication != null && authentication.isAuthenticated()) {
            response.sendRedirect("/home.html"); // Redirigir si ya está autenticado
        } else {
            if (!request.getRequestURI().equals("/index.html")) {
                response.sendRedirect("/index.html"); // Enviar a index.html si no está autenticado
            }
        }
    }

    /**
     * Cierra sesión eliminando la autenticación en Spring Security y redirigiendo a Cognito para el logout.
     */
    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Invalida la sesión local en Spring Security
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        // Redirigir a Cognito para completar el logout
        String cognitoLogoutUrl = COGNITO_LOGOUT_URL + "?client_id=" + CLIENT_ID + "&logout_uri=" + LOGOUT_REDIRECT_URI;
        response.sendRedirect(cognitoLogoutUrl);
    }
}
