package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.mongo;


import org.springframework.data.mongodb.repository.MongoRepository;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.persistence.mongo.OutboxEventMongo;

public interface MongoDataOutboxEventRepo extends MongoRepository<OutboxEventMongo, Long> {
}
