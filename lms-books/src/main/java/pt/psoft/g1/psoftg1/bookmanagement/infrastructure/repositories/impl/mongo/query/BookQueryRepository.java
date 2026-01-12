package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.mongo.query;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.dto.SearchBooksQuery;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;
import java.util.Optional;

public interface BookQueryRepository {
    List<Book> findByGenreId(String genreId);
    List<Book> findByTitle(String title);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByAuthorIds(List<String> authorsIds);
    List<Book> searchBooks(Page page, SearchBooksQuery query);
    Iterable<Book> findAll();
}
