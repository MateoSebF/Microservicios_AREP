package co.edu.eci.arep.x.controller;

import org.springframework.http.ResponseEntity;
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
     * Registra un nuevo usuario.
     * @param user Datos del usuario.
     * @return ResponseEntity con el usuario creado o error.
     */
    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User newUser = userService.createUser(user);
            return ResponseEntity.ok(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     * @param username Nombre del usuario.
     * @return ResponseEntity con el usuario o error 404 si no existe.
     */
    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
        return ResponseEntity.ok(user.get());
    }

    /**
     * Obtiene un usuario por su id.
     * @param id Id del usuario.
     * @return ResponseEntity con el usuario o error 404 si no existe.
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        Optional<User> user = userService.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
        return ResponseEntity.ok(user.get());
    }

    /**
     * Inicia sesión autenticando al usuario.
     * @param user Datos de login (email y password).
     * @return ResponseEntity con el usuario autenticado o error 401.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            User authenticatedUser = userService.authenticateUser(user.getEmail(), user.getPassword());
            return ResponseEntity.ok(authenticatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
