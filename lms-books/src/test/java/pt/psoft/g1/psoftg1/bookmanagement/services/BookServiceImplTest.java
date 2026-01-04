package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.mongo.BookMongoMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.OutboxEventRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.external.service.BookIsbnGateway;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    PhotoRepository photoRepository;

    @Mock
    BookIsbnGateway isbnGateway;

    @Mock
    BookMongoMapper bookMongoMapper;

    @Mock
    OutboxEventRepository outboxRepository;

    @InjectMocks
    BookServiceImpl bookService;

    @BeforeEach
    void setup() {
        // simular @Value
        ReflectionTestUtils.setField(bookService, "suggestionsLimitPerGenre", 2L);
    }

    // ---------- CREATE ----------

    @Test
    void createBook_success() {
        String validIsbn = "9780132350884";

        CreateBookRequest request = mock(CreateBookRequest.class);
        when(request.getTitle()).thenReturn("Clean Code");
        when(request.getDescription()).thenReturn("desc");
        when(request.getGenreId()).thenReturn("GENRE1");
        when(request.getAuthors()).thenReturn(
                List.of(new AuthorInnerRequest("1", "Robert Martin", "Bio"))
        );

        when(isbnGateway.getIsbnByTitle("Clean Code"))
                .thenReturn(Optional.of(validIsbn));

        when(bookRepository.findByIsbn(validIsbn))
                .thenReturn(Optional.empty());

        Book result = bookService.create(request);

        assertNotNull(result);
        assertEquals(validIsbn, result.getIsbn());

        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void createBook_isbnNotFound_throwsException() {
        CreateBookRequest request = mock(CreateBookRequest.class);
        when(request.getTitle()).thenReturn("Unknown");

        when(isbnGateway.getIsbnByTitle("Unknown"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookService.create(request));
    }

    @Test
    void createBook_duplicateIsbn_throwsConflict() {
        CreateBookRequest request = mock(CreateBookRequest.class);
        when(request.getTitle()).thenReturn("Clean Code");

        when(isbnGateway.getIsbnByTitle("Clean Code"))
                .thenReturn(Optional.of("123"));

        when(bookRepository.findByIsbn("123"))
                .thenReturn(Optional.of(mock(Book.class)));

        assertThrows(ConflictException.class,
                () -> bookService.create(request));
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
