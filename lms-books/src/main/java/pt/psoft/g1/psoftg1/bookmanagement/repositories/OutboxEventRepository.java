package pt.psoft.g1.psoftg1.bookmanagement.repositories;

import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;
import java.util.Optional;

public interface OutboxEventRepository {
    OutboxEventMongo save(OutboxEventMongo event);
    List<OutboxEventMongo> saveAll(List<OutboxEventMongo> events);
    Optional<OutboxEventMongo> findById(Long id);
    List<OutboxEventMongo> findAll(Page page);
    List<OutboxEventMongo> findUnprocessedEvents(Page page);
    void markAsProcessed(Long id);
}
