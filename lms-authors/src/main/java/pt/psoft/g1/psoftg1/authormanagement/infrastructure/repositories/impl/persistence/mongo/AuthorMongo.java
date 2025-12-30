package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.persistence.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "author")
public class AuthorMongo {

    @Id
    private String id;

    private String authorNumber;

    @Version
    private long version;

    private String name;

    private String bio;

    private String photo;

    public AuthorMongo() {
    }

    public AuthorMongo(String id, String authorNumber, long version, String name, String bio, String photo) {
        this.id = id;
        this.authorNumber = authorNumber;
        this.version = version;
        this.name = name;
        this.bio = bio;
        this.photo = photo;
    }
}

