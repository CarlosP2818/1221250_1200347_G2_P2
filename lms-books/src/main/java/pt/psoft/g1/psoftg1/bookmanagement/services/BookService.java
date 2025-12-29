package pt.psoft.g1.psoftg1.bookmanagement.services;

import pt.psoft.g1.psoftg1.bookmanagement.api.BookView;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
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

    List<Book> findByGenre(Long genre);

    List<Book> findByTitle(String title);

    List<Book> findByAuthorsIds(List<Long> authorsIds);

    Book removeBookPhoto(String isbn, long desiredVersion);

    List<Book> searchBooks(Page page, SearchBooksQuery query);

    Book save(Book book);
}
