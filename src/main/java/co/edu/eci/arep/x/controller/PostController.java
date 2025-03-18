package co.edu.eci.arep.x.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import co.edu.eci.arep.x.model.Post;
import co.edu.eci.arep.x.service.PostService;
import co.edu.eci.arep.x.service.StreamService;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private StreamService streamService;
    
    // Obtener todos los Posts (sin filtrar por Stream)
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    // Obtener todos los Posts de un Stream específico
    @GetMapping("/stream/{streamId}")
    public List<Post> getPostsByStream(@PathVariable String streamId) {
        return streamService.getPostsByStreamId(streamId);
    }
}
