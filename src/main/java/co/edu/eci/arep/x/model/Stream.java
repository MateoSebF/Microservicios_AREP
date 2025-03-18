package co.edu.eci.arep.x.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "streams")
@Data
public class Stream {
    @Id
    private String id;
    private String title;
    private List<String> posts;

    public Stream() {
        this.posts = new ArrayList<>();
    }

    public void addPost(Post post) {
        this.posts.add(post.getId());
    }
}
