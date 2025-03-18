package co.edu.eci.arep.x.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import co.edu.eci.arep.x.model.User;
import co.edu.eci.arep.x.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Crea un nuevo usuario con validación de unicidad.
     * @param user El usuario a registrar.
     * @return Usuario creado.
     * @throws RuntimeException si el username o email ya existen.
     */
    public User createUser(User user) {
        // Verificar si username o email ya existen
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está en uso.");
        }

        // Cifrar contraseña antes de guardarla
        user.setPassword(user.getPassword());

        try {
            return userRepository.save(user);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Error al registrar usuario: datos duplicados.");
        }
    }

    /**
     * Busca un usuario por su nombre de usuario.
     * @param username Nombre de usuario.
     * @return Optional<User> con el usuario si existe.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Busca un usuario por su id.
     * @param id Id del usuario.
     * @return Optional<User> con el usuario si existe.
     */
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    /**
     * Autentica un usuario con email y contraseña.
     * @param email Correo del usuario.
     * @param password Contraseña sin cifrar.
     * @return Usuario autenticado si la contraseña es correcta.
     * @throws RuntimeException si las credenciales no son válidas.
     */
    public User authenticateUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            return userOptional.get();
        }

        throw new RuntimeException("Credenciales inválidas.");
    }
}
