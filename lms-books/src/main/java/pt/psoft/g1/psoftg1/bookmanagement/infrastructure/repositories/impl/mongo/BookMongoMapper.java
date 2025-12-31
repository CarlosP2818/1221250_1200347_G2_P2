package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.mongo.BookMongo;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
import pt.psoft.g1.psoftg1.shared.services.IdGenerators.IdGenerator;

@Component
@Profile("mongo")
public class BookMongoMapper {

    private IdGenerator idGenerator;

    @Autowired
    public BookMongoMapper(IdGenerator injectedIdGenerator) {
        this.idGenerator = injectedIdGenerator;
    }

    public Book toDomain(BookMongo mongo) {
        if (mongo == null) return null;

        Book book = new Book(
                new Isbn(mongo.getIsbn()),
                new Title(mongo.getTitle()),
                mongo.getDescription(),
                mongo.getGenreId(),
                mongo.getAuthorsIds(),
                mongo.getPhoto() != null ? mongo.getPhoto() : null
        );

        return book;
    }

    public BookMongo toMongo(Book book) {
        if (book == null) return null;

        return new BookMongo(
                idGenerator.generateId(),
                book.getIsbn(),
                book.getTitle().getTitle(),
                book.getDescription() != null
                        ? book.getDescription()
                        : null,
                book.getGenreId(),
                book.getAuthorsIds(),
                book.getPhoto().getPhotoFile()
        );
    }
}
