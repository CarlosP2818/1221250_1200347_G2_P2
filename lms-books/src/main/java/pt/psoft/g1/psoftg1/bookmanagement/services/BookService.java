package pt.psoft.g1.psoftg1.bookmanagement.services;

import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;

/**
 *
 */
public interface BookService {

    Book create(CreateBookRequest request); // REST request

    Book create(BookViewAMQP bookViewAMQP); // AMQP request

    Book findByIsbn(String isbn);

    Book update(UpdateBookRequest request, Long currentVersion);

    Book update(BookViewAMQP bookViewAMQP);

    List<Book> findByGenre(String genre);

    List<Book> findByTitle(String title);

    List<Book> findByAuthorsIds(List<String> authorsIds);

    Book removeBookPhoto(String isbn, long desiredVersion);

    List<Book> searchBooks(Page page, SearchBooksQuery query);

    Book save(Book book);

    OutboxEventMongo createTemp(CreateBookRequest request, String photoURI, String correlationId);

}
