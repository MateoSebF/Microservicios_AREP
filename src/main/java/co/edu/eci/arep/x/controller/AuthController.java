package co.edu.eci.arep.x.controller;

import java.io.IOException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AuthController {

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Invalida la sesión actual
        request.getSession().invalidate();

        // Limpia el contexto de autenticación
        SecurityContextHolder.clearContext();

        // Redirige a Cognito para cerrar sesión completamente
        String clientId = "57a363flss9rbedmjc6g16gd9n"; // Reemplázalo con tu Client ID real
        String logoutRedirectUri = "https://backendeci.duckdns.org:8080/index.html";
        String logoutUrl = "https://us-east-13lagh3i29.auth.us-east-1.amazoncognito.com/logout"
                + "?client_id=" + clientId
                + "&logout_uri=" + logoutRedirectUri;

        response.sendRedirect(logoutUrl);
    }
}
