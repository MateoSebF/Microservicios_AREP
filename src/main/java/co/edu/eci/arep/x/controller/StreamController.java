package co.edu.eci.arep.x.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import co.edu.eci.arep.x.model.Stream;
import co.edu.eci.arep.x.model.Post;
import co.edu.eci.arep.x.service.StreamService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/streams")
public class StreamController {

    @Autowired
    private StreamService streamService;

    // Crear un nuevo Stream
    @PostMapping
    public Stream createStream(@RequestBody Stream stream) {
        return streamService.createStream(stream);
    }

    // Obtener todos los Streams
    @GetMapping
    public List<Stream> getStreams() {
        return streamService.getAllStreams();
    }

    // Obtener un Stream por ID
    @GetMapping("/{id}")
    public Optional<Stream> getStream(@PathVariable String id) {
        return streamService.getStreamById(id);
    }

    // Agregar un Post dentro de un Stream
    @PostMapping("/{id}/posts")
    public Stream addPostToStream(@PathVariable String id, @RequestBody Post post) {
        return streamService.addPostToStream(id, post);
    }

    // Obtener todos los Posts de un Stream
    @GetMapping("/{id}/posts")
    public List<Post> getPostsFromStream(@PathVariable String id) {
        return streamService.getPostsByStreamId(id);
    }
}
