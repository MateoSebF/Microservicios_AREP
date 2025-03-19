package co.edu.eci.arep.x.controller;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AuthController {

    @GetMapping("/")
    public void redirectIfAuthenticated(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication != null && authentication.isAuthenticated()) {
            response.sendRedirect("/home.html"); // Redirigir si ya está autenticado
        } else {
            response.sendRedirect("/index.html"); // Enviar a index.html si no está autenticado
        }
    }
}
