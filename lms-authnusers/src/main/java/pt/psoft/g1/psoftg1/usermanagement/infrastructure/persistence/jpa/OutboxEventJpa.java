package pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_events")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OutboxEventJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateType; // ex: "User"
    private String aggregateId;   // ex: userId
    private String type;          // ex: "USER_CREATED"
    private String payload;       // JSON com os dados
    private boolean processed;    // false por padrão
    private LocalDateTime createdAt;

}
