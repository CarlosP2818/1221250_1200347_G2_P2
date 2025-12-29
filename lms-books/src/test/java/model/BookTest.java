package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookTest {

    private final Isbn validIsbn = new Isbn("9782826012092");
    private final Title validTitle = new Title("Encantos de contar");
    private final Long validGenreId = 1L;
    private List<Long> authorsIds;

    @BeforeEach
    void setUp() {
        authorsIds = new ArrayList<>();
    }

    @Test
    void ensureIsbnNotNull() {
        authorsIds.add(1L);
        assertThrows(IllegalArgumentException.class, () ->
                new Book(null, validTitle, null, validGenreId, authorsIds, null));
    }

    @Test
    void ensureTitleNotNull() {
        authorsIds.add(1L);
        assertThrows(IllegalArgumentException.class, () ->
                new Book(validIsbn, null, null, validGenreId, authorsIds, null));
    }

    @Test
    void ensureGenreNotNull() {
        authorsIds.add(1L);
        assertThrows(IllegalArgumentException.class, () ->
                new Book(validIsbn, validTitle, null, null, authorsIds, null));
    }

    @Test
    void ensureAuthorsNotNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Book(validIsbn, validTitle, null, validGenreId, null, null));
    }

    @Test
    void ensureAuthorsNotEmpty() {
        assertThrows(IllegalArgumentException.class, () ->
                new Book(validIsbn, validTitle, null, validGenreId, authorsIds, null));
    }

    @Test
    void ensureBookCreatedWithMultipleAuthors() {
        authorsIds.add(1L);
        authorsIds.add(2L);
        assertDoesNotThrow(() ->
                new Book(validIsbn, validTitle, null, validGenreId, authorsIds, null));
    }
}
