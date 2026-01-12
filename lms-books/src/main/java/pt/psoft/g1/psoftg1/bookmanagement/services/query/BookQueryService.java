package pt.psoft.g1.psoftg1.bookmanagement.services.query;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.dto.SearchBooksQuery;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;

public interface BookQueryService {
    Book findByIsbn(String isbn);
    List<Book> getBooksSuggestionsForReader(List<String> genreIds);
    List<Book> findByGenre(String genre);
    List<Book> findByTitle(String title);
    List<Book> findByAuthorsIds(List<String> authorsIds);
    List<Book> searchBooks(Page page, SearchBooksQuery query);
}
