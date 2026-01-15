package pt.psoft.g1.psoftg1.authormanagement.services.query;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorQueryServiceImplTest {

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    AuthorQueryServiceImpl authorService;

    Author testAuthor;

    @BeforeEach
    void setup() {
        testAuthor = new Author("Alex", "O Alex escreveu livros", "photo.jpg");
    }

    // -------------------- FIND --------------------

    @Test
    void findByAuthorNumber_found() {
        when(authorRepository.findByAuthorNumber("1")).thenReturn(Optional.of(testAuthor));

        Optional<Author> result = authorService.findByAuthorNumber("1");
        assertTrue(result.isPresent());
        assertEquals("Alex", result.get().getName());
    }

    @Test
    void findByAuthorNumber_notFound() {
        when(authorRepository.findByAuthorNumber("999")).thenReturn(Optional.empty());

        Optional<Author> result = authorService.findByAuthorNumber("999");
        assertFalse(result.isPresent());
    }

    // -------------------- MODEL APPLY PATCH --------------------

    @Test
    void author_applyPatch_updatesFields() {
        Author author = new Author("Old", "OldBio", null);
        UpdateAuthorRequest request = new UpdateAuthorRequest("Bio", "Name", null, null);

        long version = author.getVersion();
        author.applyPatch(version, request);

        assertEquals("Name", author.getName());
        assertEquals("Bio", author.getBio());
    }

    @Test
    void author_applyPatch_staleVersion_throws() {
        Author author = new Author("Old", "OldBio", null);
        UpdateAuthorRequest request = new UpdateAuthorRequest("New", "NewName", null, null);

        assertThrows(StaleObjectStateException.class,
                () -> author.applyPatch(999, request));
    }

    @Test
    void author_removePhoto_correctVersion() {
        Author author = new Author("Alex", "Bio", "photo.jpg");
        author.removePhoto(author.getVersion());
        assertNull(author.getPhoto());
    }

    @Test
    void author_removePhoto_incorrectVersion_throws() {
        Author author = new Author("Alex", "Bio", "photo.jpg");
        assertThrows(ConflictException.class,
                () -> author.removePhoto(author.getVersion() + 1));
    }

    @Test
    void author_createWithNullPhoto_setsNull() {
        Author author = new Author("Alex", "Bio", null);
        assertNull(author.getPhoto());
    }

    @Test
    void author_createWithPhoto_setsPhoto() {
        Author author = new Author("Alex", "Bio", "photo.jpg");
        assertNotNull(author.getPhoto());
        assertEquals("photo.jpg", author.getPhoto().getPhotoFile());
    }

}