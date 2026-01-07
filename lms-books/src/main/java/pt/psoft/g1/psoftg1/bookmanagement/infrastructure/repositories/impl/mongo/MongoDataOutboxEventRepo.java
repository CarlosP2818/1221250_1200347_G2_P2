package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.mongo.OutboxEventMongo;

import java.util.Optional;

public interface MongoDataOutboxEventRepo extends MongoRepository<OutboxEventMongo, Long> {
    Optional<OutboxEventMongo> findByCorrelationId(String correlationId);
}
