package pt.psoft.g1.psoftg1.bookmanagement.services.query;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.mongo.query.BookQueryRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.dto.SearchBooksQuery;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookQueryServiceImpl implements BookQueryService{

    private final BookQueryRepository bookQueryRepository;

    @Value("${suggestionsLimitPerGenre}")
    private long suggestionsLimitPerGenre;

    @Override
    @Cacheable(value = "books", key = "#isbn")
    public Book findByIsbn(String isbn) {
        return bookQueryRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NotFoundException(Book.class, isbn));
    }

    public List<Book> getBooksSuggestionsForReader(List<String> genreIds) {
        List<Book> books = new ArrayList<>();

        for (String genreId : genreIds) {
            List<Book> tempBooks = bookQueryRepository.findByGenreId(genreId);
            if (tempBooks.isEmpty()) continue;

            long genreBookCount = 0;
            for (Book loopBook : tempBooks) {
                if (genreBookCount >= suggestionsLimitPerGenre) break;
                books.add(loopBook);
                genreBookCount++;
            }
        }

        return books;
    }

    @Override
    public List<Book> searchBooks(Page page, SearchBooksQuery query) {
        if (page == null) {
            page = new Page(1, 10);
        }
        if (query == null) {
            query = new SearchBooksQuery("", "", "");
        }
        return bookQueryRepository.searchBooks(page, query);
    }

    @Override
    @Cacheable(value = "booksByGenre", key = "#genre")
    public List<Book> findByGenre(String genre) {
        return bookQueryRepository.findByGenreId(genre);
    }

    @Override
    @Cacheable(value = "booksByTitle", key = "#title")
    public List<Book> findByTitle(String title) {
        return bookQueryRepository.findByTitle(title);
    }

    @Override
    @Cacheable(value = "booksByAuthor", key = "#authorName")
    public List<Book> findByAuthorsIds(List<String> authorsIds) {
        return bookQueryRepository.findByAuthorIds(authorsIds);
    }

}
