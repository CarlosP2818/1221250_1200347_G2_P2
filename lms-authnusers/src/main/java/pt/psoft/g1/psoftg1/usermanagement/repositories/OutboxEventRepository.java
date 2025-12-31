package pt.psoft.g1.psoftg1.usermanagement.repositories;

import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.jpa.OutboxEventJpa;

import java.util.List;
import java.util.Optional;

public interface OutboxEventRepository {
    OutboxEventJpa save(OutboxEventJpa event);
    List<OutboxEventJpa> saveAll(List<OutboxEventJpa> events);
    Optional<OutboxEventJpa> findById(Long id);
    List<OutboxEventJpa> findAll(Page page);
    List<OutboxEventJpa> findUnprocessedEvents(Page page);
    void markAsProcessed(Long id);
}
