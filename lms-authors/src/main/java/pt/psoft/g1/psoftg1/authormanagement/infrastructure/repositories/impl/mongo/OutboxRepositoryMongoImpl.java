package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.mongo;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.authormanagement.repositories.OutboxEventRepository;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class OutboxRepositoryMongoImpl implements OutboxEventRepository {

    private final MongoTemplate mongoTemplate;
    private final MongoDataOutboxEventRepo repo;

    @Override
    public OutboxEventMongo save(OutboxEventMongo event) {
        return repo.save(event);
    }

    @Override
    public List<OutboxEventMongo> saveAll(List<OutboxEventMongo> events) {
        return repo.saveAll(events);
    }

    @Override
    public Optional<OutboxEventMongo> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<OutboxEventMongo> findAll(Page page) {
        Query query = new Query()
                .with(Sort.by(Sort.Direction.DESC, "createdAt"))
                .skip((long) (page.getNumber() - 1) * page.getLimit())
                .limit(page.getLimit());

        return mongoTemplate.find(query, OutboxEventMongo.class);
    }


    public List<OutboxEventMongo> findUnprocessedEvents(Page page) {
        Query query = new Query(
                Criteria.where("processed").is(false)
        )
                .with(Sort.by(Sort.Direction.ASC, "createdAt"))
                .skip((long) (page.getNumber() - 1) * page.getLimit())
                .limit(page.getLimit());

        return mongoTemplate.find(query, OutboxEventMongo.class);
    }

    @Override
    public void markAsProcessed(Long id) {
        repo.findById(id).ifPresent(event -> {
            event.setProcessed(true);
            repo.save(event);
        });
    }

}
