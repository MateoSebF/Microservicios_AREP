package co.edu.eci.arep.x.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.eci.arep.x.model.Post;
import co.edu.eci.arep.x.repository.PostRepository;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // Obtener todos los posts en orden descendente
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByTimestampDesc();
    }
}
