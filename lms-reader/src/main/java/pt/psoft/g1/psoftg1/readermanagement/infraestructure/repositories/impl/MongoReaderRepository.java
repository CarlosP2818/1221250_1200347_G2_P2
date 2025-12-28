package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl;

//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Repository;
//import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
//import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
//import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
//import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//@Profile("mongo")
//@RequiredArgsConstructor
//public class MongoReaderRepository implements ReaderRepository {
//
//    private final MongoTemplate mongoTemplate;
//
//    @Override
//    public Optional<ReaderDetails> findByReaderNumber(String readerNumber) {
//        Query query = new Query(Criteria.where("readerNumber.readerNumber").is(readerNumber));
//        return Optional.ofNullable(mongoTemplate.findOne(query, ReaderDetails.class));
//    }
//
//    @Override
//    public List<ReaderDetails> findByPhoneNumber(String phoneNumber) {
//        Query query = new Query(Criteria.where("phoneNumber.phoneNumber").is(phoneNumber));
//        return mongoTemplate.find(query, ReaderDetails.class);
//    }
//
//    @Override
//    public Optional<ReaderDetails> findByUsername(String username) {
//        Query query = new Query(Criteria.where("reader.username").is(username));
//        return Optional.ofNullable(mongoTemplate.findOne(query, ReaderDetails.class));
//    }
//
//    @Override
//    public Optional<ReaderDetails> findByUserId(Long userId) {
//        Query query = new Query(Criteria.where("reader.id").is(userId));
//        return Optional.ofNullable(mongoTemplate.findOne(query, ReaderDetails.class));
//    }
//
//    @Override
//    public int getCountFromCurrentYear() {
//        LocalDate startOfYear = LocalDate.now().withDayOfYear(1);
//        Query query = new Query(Criteria.where("reader.createdAt").gte(startOfYear));
//        return (int) mongoTemplate.count(query, ReaderDetails.class);
//    }
//
//    @Override
//    public ReaderDetails save(ReaderDetails readerDetails) {
//        return mongoTemplate.save(readerDetails);
//    }
//
//    @Override
//    public Iterable<ReaderDetails> findAll() {
//        return mongoTemplate.findAll(ReaderDetails.class);
//    }
//
//    @Override
//    public void delete(ReaderDetails readerDetails) {
//        mongoTemplate.remove(readerDetails);
//    }
//
//    @Override
//    public List<ReaderDetails> searchReaderDetails(pt.psoft.g1.psoftg1.shared.services.Page page, SearchReadersQuery query) {
//        Query mongoQuery = new Query();
//
//        if (query.getName() != null && !query.getName().isBlank()) {
//            mongoQuery.addCriteria(Criteria.where("reader.name.name").regex(".*" + query.getName() + ".*", "i"));
//        }
//        if (query.getEmail() != null && !query.getEmail().isBlank()) {
//            mongoQuery.addCriteria(Criteria.where("reader.username").is(query.getEmail()));
//        }
//        if (query.getPhoneNumber() != null && !query.getPhoneNumber().isBlank()) {
//            mongoQuery.addCriteria(Criteria.where("phoneNumber.phoneNumber").is(query.getPhoneNumber()));
//        }
//
//        mongoQuery.with(Sort.by(Sort.Direction.ASC, "reader.name.name"));
//        mongoQuery.skip((page.getNumber() - 1) * page.getLimit());
//        mongoQuery.limit(page.getLimit());
//
//        return mongoTemplate.find(mongoQuery, ReaderDetails.class);
//    }
//
//    @Override
//    public Page<ReaderDetails> findTopReaders(Pageable pageable) {
//        // Como MongoDB não suporta Page diretamente sem usar Spring Data MongoRepository,
//        // podemos implementar manualmente ou usar Spring Data MongoRepository + Pageable.
//        throw new UnsupportedOperationException("Implement with MongoRepository or Aggregation.");
//    }
//
//    @Override
//    public Page<ReaderBookCountDTO> findTopByGenre(Pageable pageable, String genre, LocalDate startDate, LocalDate endDate) {
//        throw new UnsupportedOperationException("Implement with Aggregation Framework in MongoDB.");
//    }
//}
//