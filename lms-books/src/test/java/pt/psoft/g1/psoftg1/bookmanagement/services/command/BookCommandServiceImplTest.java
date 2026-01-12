package pt.psoft.g1.psoftg1.bookmanagement.services.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.mongo.command.BookCommandRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.AuthorInnerRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.dto.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.dto.UpdateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.query.BookQueryService;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.external.service.BookIsbnGateway;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookCommandServiceImplTest {

    @Mock
    BookCommandRepository bookCommandRepository;
    @Mock
    BookIsbnGateway isbnGateway;
    @Mock
    BookQueryService bookQueryService;

    @InjectMocks
    BookCommandServiceImpl bookService;

    // ---------- CREATE ----------
    @Test
    void createBook_Success() {
        String title = "Clean Code";
        String isbn = "9780132350884";
        CreateBookRequest request = mock(CreateBookRequest.class);
        when(request.getTitle()).thenReturn(title);
        when(request.getDescription()).thenReturn("Description");
        when(request.getGenreName()).thenReturn("Software");
        when(request.getAuthors()).thenReturn(List.of(new AuthorInnerRequest("1", "Robert Martin", "Bio")));

        when(isbnGateway.getIsbnByTitle(title)).thenReturn(Optional.of(isbn));

        when(bookQueryService.findByIsbn(isbn)).thenThrow(new NotFoundException("Not found"));

        Book result = bookService.create(request);

        assertNotNull(result);
        assertEquals(isbn, result.getIsbn());
        verify(bookCommandRepository).save(any(Book.class));
    }

    @Test
    void createBook_ThrowsConflict_WhenBookExists() {
        // Arrange
        String title = "Clean Code";
        String isbn = "9780132350884";
        CreateBookRequest request = mock(CreateBookRequest.class);
        when(request.getTitle()).thenReturn(title);
        when(isbnGateway.getIsbnByTitle(title)).thenReturn(Optional.of(isbn));

        when(bookQueryService.findByIsbn(isbn)).thenReturn(mock(Book.class));

        assertThrows(ConflictException.class, () -> bookService.create(request));
    }

    @Test
    void updateBook_Success() {
        String isbn = "9780132350884";
        UpdateBookRequest request = new UpdateBookRequest(isbn, "New Title", "New Genre", List.of("Auth1"), "New Desc");
        Book existingBook = mock(Book.class);

        when(bookQueryService.findByIsbn(isbn)).thenReturn(existingBook);
        when(bookCommandRepository.save(any(Book.class))).thenReturn(existingBook);

        Book result = bookService.update(request, 1L);

        assertNotNull(result);
        verify(existingBook).setTitle(any());
        verify(existingBook).setDescription(any());
        verify(bookCommandRepository).save(existingBook);
    }

    @Test
    void removeBookPhoto_ThrowsNotFound_WhenNoPhotoExists() {
        String isbn = "123456789";
        Book book = mock(Book.class);
        when(bookQueryService.findByIsbn(isbn)).thenReturn(book);
        when(book.getPhoto()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> bookService.removeBookPhoto(isbn, 1L));
    }
}