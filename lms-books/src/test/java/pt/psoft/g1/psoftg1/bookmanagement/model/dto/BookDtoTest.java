package pt.psoft.g1.psoftg1.bookmanagement.model.dto;

import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookStatus;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BookDtoTest {

    @Test
    void testAllSettersAndGetters() {
        BookDto book = new BookDto();

        String isbn = "978-3-16-148410-0";
        String title = "Java for Professionals";
        String description = "Comprehensive guide to Java";
        String genreId = "1";
        List<String> authorsIds = Arrays.asList("author1", "author2");
        String photoURI = "photo.jpg";
        Long version = 1L;
        UUID sagaId = UUID.randomUUID();
        BookStatus status = BookStatus.STARTED;

        // Set values
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setDescription(description);
        book.setGenreId(genreId);
        book.setAuthorsIds(authorsIds);
        book.setPhotoURI(photoURI);
        book.setVersion(version);
        book.setSagaId(sagaId);
        book.setStatus(status);

        // Assert values
        assertEquals(isbn, book.getIsbn());
        assertEquals(title, book.getTitle());
        assertEquals(description, book.getDescription());
        assertEquals(genreId, book.getGenreId());
        assertEquals(authorsIds, book.getAuthorsIds());
        assertEquals(photoURI, book.getPhotoURI());
        assertEquals(version, book.getVersion());
        assertEquals(sagaId, book.getSagaId());
        assertEquals(status, book.getStatus());
    }

    @Test
    void testAllArgsConstructor() {
        List<String> authorsIds = Arrays.asList("a1", "a2");
        UUID sagaId = UUID.randomUUID();
        BookDto book = new BookDto(
                "978-3-16-148410-0",
                "Java Book",
                "Description",
                "2",
                authorsIds,
                "photo.jpg",
                1L,
                sagaId,
                BookStatus.STARTED
        );

        assertEquals("978-3-16-148410-0", book.getIsbn());
        assertEquals("Java Book", book.getTitle());
        assertEquals(authorsIds, book.getAuthorsIds());
        assertEquals(BookStatus.STARTED, book.getStatus());
    }

    @Test
    void testBuilder() {
        List<String> authorsIds = Arrays.asList("a1", "a2");
        UUID sagaId = UUID.randomUUID();

        BookDto book = BookDto.builder()
                .isbn("978-3-16-148410-0")
                .title("Builder Book")
                .description("Description")
                .genreId("3")
                .authorsIds(authorsIds)
                .photoURI("photoBuilder.jpg")
                .version(2L)
                .sagaId(sagaId)
                .status(BookStatus.STARTED)
                .build();

        assertEquals("Builder Book", book.getTitle());
        assertEquals("photoBuilder.jpg", book.getPhotoURI());
        assertEquals(BookStatus.STARTED, book.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID sagaId = UUID.randomUUID();
        BookDto book1 = new BookDto("1", "Book1", "Desc", "1", List.of("a1"), null, 1L, sagaId, BookStatus.STARTED);
        BookDto book2 = new BookDto("1", "Book1", "Desc", "1", List.of("a1"), null, 1L, sagaId, BookStatus.STARTED);

        assertEquals(book1, book2);
        assertEquals(book1.hashCode(), book2.hashCode());
    }

    @Test
    void testToString() {
        BookDto book = new BookDto();
        book.setTitle("Some Title");
        String str = book.toString();
        assertTrue(str.contains("Some Title"));
    }
}
