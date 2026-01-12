package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.mongo.command;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.mongo.BookMongo;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.mongo.BookMongoMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;

@Repository
@Profile("mongo")
@RequiredArgsConstructor
public class BookCommandRepositoryMongoImpl implements BookCommandRepository{

    private final MongoTemplate mongoTemplate;

    private final BookMongoMapper bookMongoMapper;

    @Override
    public Book save(Book book) {
        BookMongo bookMongoDB = bookMongoMapper.toMongo(book);
        BookMongo saved = mongoTemplate.save(bookMongoDB);
        return bookMongoMapper.toDomain(saved);
    }

    @Override
    public void delete(Book book) {
        Query query = new Query(Criteria.where("isbn").is(book.getIsbn()));
        mongoTemplate.remove(query, BookMongo.class);
    }
}
