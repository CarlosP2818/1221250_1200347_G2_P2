package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.persistence.mongo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "author_temp")
public class OutboxEventMongo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter @Setter
    private String bio;

    @Getter @Setter
    private UUID sagaId;

    @Getter @Setter
    private boolean processed = false;
}
