package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.persistence.mongo;

import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;

@Document(collection = "genre")
public class GenreMongo {

    private static final int GENRE_MAX_LENGTH = 100;

    @Id
    @Getter
    private String pk; // Mongo ObjectId

    @Size(min = 1, max = GENRE_MAX_LENGTH, message = "Genre name must be between 1 and 100 characters")
    @Indexed(unique = true)
    @Getter
    private String genre;

    protected GenreMongo() {
        // Construtor default para MongoDB
    }

    public GenreMongo(String pk,String genre) {
        this.pk = pk;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return genre;
    }
}
