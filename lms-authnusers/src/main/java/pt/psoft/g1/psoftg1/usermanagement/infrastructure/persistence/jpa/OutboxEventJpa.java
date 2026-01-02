package pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @Column(nullable = false)
    private String aggregateType;

    @Column(nullable = false)
    private String aggregateId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String correlationId;

    @Column(nullable = false)
    private boolean processed = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}

