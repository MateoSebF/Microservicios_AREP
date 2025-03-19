package co.edu.eci.arep.x.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import co.edu.eci.arep.x.model.User;
import co.edu.eci.arep.x.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registra el usuario en la base de datos si no existe y retorna sus datos.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal OidcUser oidcUser) {
        if (oidcUser == null) {
            return ResponseEntity.status(401).body("No autenticado");
        }
        System.out.println("OIDC User Attributes: " + oidcUser.getAttributes());

        String email = oidcUser.getEmail();
        String username = (String) oidcUser.getAttributes().get("cognito:username");
        if (username == null) {
            username = (String) oidcUser.getAttributes().get("username");
        }
        if (username == null) {
            username = email; // Fallback al email si no hay username
        }


        User user = userService.registerUserIfNotExists(username, email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(404).body("Usuario no encontrado");
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(404).body("Usuario no encontrado"); 
    }
}
