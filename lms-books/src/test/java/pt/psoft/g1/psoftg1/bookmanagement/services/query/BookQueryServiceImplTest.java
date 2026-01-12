package pt.psoft.g1.psoftg1.bookmanagement.services.query;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.mongo.query.BookQueryRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookQueryServiceImplTest {

    @Mock
    BookQueryRepository bookRepository;

    @InjectMocks
    BookQueryServiceImpl bookService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(bookService, "suggestionsLimitPerGenre", 2L);
    }

    // ---------- FIND BY ISBN ----------

    @Test
    void findByIsbn_success() {
        Book book = mock(Book.class);

        when(bookRepository.findByIsbn("123"))
                .thenReturn(Optional.of(book));

        Book result = bookService.findByIsbn("123");

        assertEquals(book, result);
    }

    @Test
    void findByIsbn_notFound() {
        when(bookRepository.findByIsbn("999"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookService.findByIsbn("999"));
    }

    // ---------- SUGGESTIONS ----------

    @Test
    void getBooksSuggestionsForReader_respectsLimitPerGenre() {
        Book book1 = mock(Book.class);
        Book book2 = mock(Book.class);
        Book book3 = mock(Book.class);

        when(bookRepository.findByGenreId("GENRE1"))
                .thenReturn(List.of(book1, book2, book3));

        List<Book> result =
                bookService.getBooksSuggestionsForReader(List.of("GENRE1"));

        assertEquals(2, result.size()); // limit = 2
    }

}