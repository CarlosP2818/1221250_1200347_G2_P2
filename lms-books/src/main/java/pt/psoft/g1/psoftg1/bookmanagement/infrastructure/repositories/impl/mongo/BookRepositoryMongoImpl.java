package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.mongo.BookMongo;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("mongo")
@RequiredArgsConstructor
public class BookRepositoryMongoImpl implements BookRepository {

    private final MongoTemplate mongoTemplate;

    private final BookMongoMapper bookMongoMapper;

    @Override
    public List<Book> findByGenreId(String genreId) {
        Query query = new Query(Criteria.where("genreId").is(genreId));
        return mongoTemplate.find(query, BookMongo.class)
                .stream()
                .map(bookMongoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByTitle(String title) {
        Query query = new Query(Criteria.where("title").regex(title, "i"));
        List<BookMongo> books = mongoTemplate.find(query, BookMongo.class);
        return books.stream()
                .map(bookMongoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        Query query = new Query(Criteria.where("isbn").is(isbn));
        BookMongo book = mongoTemplate.findOne(query, BookMongo.class);
        return Optional.ofNullable(book).map(bookMongoMapper::toDomain);
    }

    @Override
    public List<Book> findByAuthorIds(List<String> authorsIds) {
        Query query = new Query(
                Criteria.where("authorsIds").in(authorsIds)
        );
        return mongoTemplate.find(query, BookMongo.class)
                .stream()
                .map(bookMongoMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<Book> searchBooks(Page page, SearchBooksQuery query) {

        Criteria criteria = new Criteria();

        if (query.getTitle() != null && !query.getTitle().isBlank()) {
            criteria = criteria.and("title")
                    .regex("^" + query.getTitle(), "i");
        }

        if (query.getGenreName() != null && !query.getGenreName().isBlank()) {
            criteria = criteria.and("genreId")
                    .is(query.getGenreName());
        }

        if (query.getAuthorIds() != null && !query.getAuthorIds().isEmpty()) {
            criteria = criteria.and("authorIds")
                    .in(query.getAuthorIds());
        }

        Query mongoQuery = new Query(criteria)
                .skip((long) (page.getNumber() - 1) * page.getLimit())
                .limit(page.getLimit());

        return mongoTemplate.find(mongoQuery, BookMongo.class)
                .stream()
                .map(bookMongoMapper::toDomain)
                .collect(Collectors.toList());
    }


    @Override
    public Book save(Book book) {
        BookMongo bookMongoDB = bookMongoMapper.toMongo(book);
        BookMongo saved = mongoTemplate.save(bookMongoDB);
        return bookMongoMapper.toDomain(saved);
    }

    @Override
    public Iterable<Book> findAll() {
        return null;
    }

    @Override
    public void delete(Book book) {
        Query query = new Query(Criteria.where("isbn").is(book.getIsbn()));
        mongoTemplate.remove(query, BookMongo.class);
    }
}
