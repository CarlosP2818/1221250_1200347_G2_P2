package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.mongo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "outbox_events")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OutboxEventMongo {

    @Id
    private Long id;

    private String aggregateType;

    private String aggregateId;

    private String type;

    private String correlationId;

    private boolean processed = false;

    private LocalDateTime createdAt;
}