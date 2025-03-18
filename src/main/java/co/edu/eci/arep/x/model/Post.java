package co.edu.eci.arep.x.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "posts")
@Data
public class Post {
    @Id
    private String id;
    private String userId;
    private String content;
    private Instant timestamp;

    // Constructor que asigna la fecha automáticamente
    public Post() {
        this.timestamp = Instant.now();
    }
}
