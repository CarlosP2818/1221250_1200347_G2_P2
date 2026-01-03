package pt.psoft.g1.psoftg1.bookmanagement.repositories;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface BookRepository {

    List<Book> findByGenreId(String genreId);

    List<Book> findByTitle(String title);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByAuthorIds(List<String> authorsIds);

    List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query);

    Book save(Book book);

    Iterable<Book> findAll();

    void delete(Book book);
}
