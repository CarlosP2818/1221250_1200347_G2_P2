package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.persistence.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id; // Import correto do Spring Data
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.UUID;

@Document(collection = "author_temp")
@Getter
@Setter
public class OutboxEventMongo {

    @Id
    private String id;

    private String name;
    private String bio;
    private UUID sagaId;
    private boolean processed = false;

    public OutboxEventMongo() {}
}