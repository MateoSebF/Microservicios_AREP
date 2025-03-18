package co.edu.eci.arep.x.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.eci.arep.x.model.Stream;
import co.edu.eci.arep.x.model.Post;
import co.edu.eci.arep.x.repository.StreamRepository;
import co.edu.eci.arep.x.repository.PostRepository;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@Service
public class StreamService {

    @Autowired
    private StreamRepository streamRepository;
    @Autowired
    private PostRepository postRepository;

    // Crear un nuevo Stream
    public Stream createStream(Stream stream) {
        return streamRepository.save(stream);
    }

    // Obtener todos los Streams
    public List<Stream> getAllStreams() {
        return streamRepository.findAll();
    }

    // Obtener un Stream por ID
    public Optional<Stream> getStreamById(String id) {
        return streamRepository.findById(id);
    }

    // Agregar un Post a un Stream
    public Stream addPostToStream(String streamId, Post post) {
        Optional<Stream> streamOptional = streamRepository.findById(streamId);
        if (streamOptional.isPresent()) {
            Stream stream = streamOptional.get();
            post = postRepository.save(post); // Guardar el post en la BD
            stream.addPost(post); // Agregar el ID del post al stream
            return streamRepository.save(stream); // Guardar cambios en la BD
        } else {
            throw new RuntimeException("Stream not found");
        }
    }

    // Obtener todos los Posts de un Stream
    public List<Post> getPostsByStreamId(String streamId) {
        Optional<Stream> streamOptional = streamRepository.findById(streamId);
        if (!streamOptional.isPresent()) {
            throw new RuntimeException("Stream not found");
        }
        Stream stream = streamOptional.get();
        List<Post> posts = new ArrayList<>();
        for (String postId : stream.getPosts()) {
            Optional<Post> postOptional = postRepository.findById(postId);
            postOptional.ifPresent(posts::add);
        }
        return posts;
    }
}
