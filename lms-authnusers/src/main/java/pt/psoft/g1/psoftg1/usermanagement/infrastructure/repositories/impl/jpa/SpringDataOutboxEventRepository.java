package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.jpa.OutboxEventJpa;

public interface SpringDataOutboxEventRepository extends JpaRepository<OutboxEventJpa, Long> {
}
