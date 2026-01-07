package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.persistence.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "genre_temp")
@Getter
@Setter
public class OutboxEventMongo {

    @Id
    private String id;
    private String name;
    private String correlationId;
    private boolean processed = false;
}