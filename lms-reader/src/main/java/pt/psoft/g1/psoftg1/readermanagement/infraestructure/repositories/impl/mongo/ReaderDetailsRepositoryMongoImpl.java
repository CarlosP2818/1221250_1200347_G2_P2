package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.mongo.ReaderDetailsMongo;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Profile("mongo")
public class ReaderDetailsRepositoryMongoImpl implements ReaderRepository {

    private final MongoTemplate mongoTemplate;

    private final ReaderDetailsMongoMapper readerDetailsMongoMapper;

    @Override
    public Optional<ReaderDetails> findByReaderNumber(String readerNumber) {
        Query query = new Query(Criteria.where("readerNumber.readerNumber").is(readerNumber));
        ReaderDetailsMongo readerMongo = mongoTemplate.findOne(query, ReaderDetailsMongo.class);
        return Optional.ofNullable(readerMongo != null ? readerDetailsMongoMapper.toDomain(readerMongo) : null);
    }

    @Override
    public List<ReaderDetails> findByPhoneNumber(String phoneNumber) {
        Query query = new Query(Criteria.where("phoneNumber").is(phoneNumber));
        return mongoTemplate.find(query, ReaderDetails.class);
    }

    @Override
    public Optional<ReaderDetails> findByUsername(String username) {
        Query query = new Query(Criteria.where("user.username").is(username));
        ReaderDetails reader = mongoTemplate.findOne(query, ReaderDetails.class);
        return Optional.ofNullable(reader);
    }

    @Override
    public Optional<ReaderDetails> findByUserId(String userId) {
        Query query = new Query(Criteria.where("user.id").is(userId));
        ReaderDetails reader = mongoTemplate.findOne(query, ReaderDetails.class);
        return Optional.ofNullable(reader);
    }

    @Override
    public int getCountFromCurrentYear() {
        int currentYear = LocalDate.now().getYear();
        Query query = new Query(Criteria.where("registrationYear").is(currentYear));
        return (int) mongoTemplate.count(query, ReaderDetails.class);
    }

    @Override
    public ReaderDetails save(ReaderDetails readerDetails) {
        return mongoTemplate.save(readerDetails);
    }

    @Override
    public Iterable<ReaderDetails> findAll() {
        return mongoTemplate.findAll(ReaderDetails.class);
    }

    @Override
    public Page<ReaderDetails> findTopReaders(Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReaderBookCountDTO> findTopByGenre(Pageable pageable, String genre, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public void delete(ReaderDetails readerDetails) {
        Query query = new Query(Criteria.where("readerNumber").is(readerDetails.getReaderNumber()));
        mongoTemplate.remove(query, ReaderDetails.class);
    }

    @Override
    public List<ReaderDetails> searchReaderDetails(pt.psoft.g1.psoftg1.shared.services.Page page, SearchReadersQuery query) {
        Query mongoQuery = new Query();

        if (query.getName() != null && !query.getName().isBlank()) {
            mongoQuery.addCriteria(Criteria.where("user.name").is(query.getName()));
        }
        if (query.getEmail() != null && !query.getEmail().isBlank()) {
            mongoQuery.addCriteria(Criteria.where("user.email").is(query.getEmail()));
        }
        if (query.getPhoneNumber() != null && !query.getPhoneNumber().isBlank()) {
            mongoQuery.addCriteria(Criteria.where("phoneNumber").is(query.getPhoneNumber()));
        }

        mongoQuery.skip(page.getNumber());
        mongoQuery.limit(page.getLimit());

        return mongoTemplate.find(mongoQuery, ReaderDetails.class);
    }
}
