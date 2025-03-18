package co.edu.eci.arep.x.repository;

import co.edu.eci.arep.x.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findAllByOrderByTimestampDesc();
}
