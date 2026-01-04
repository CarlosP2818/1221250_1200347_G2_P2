package pt.psoft.g1.psoftg1.authormanagement.services;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.repositories.OutboxEventRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplMutationTest {

    @Mock
    AuthorRepository authorRepository;

    @Mock
    AuthorMapper mapper;

    @Mock
    PhotoRepository photoRepository;

    @Mock
    RabbitMQPublisher rabbitMQPublisher;

    @Mock
    OutboxEventRepository tempAuthorRepository;

    @InjectMocks
    AuthorServiceImpl authorService;

    Author testAuthor;

    @BeforeEach
    void setup() {
        testAuthor = new Author("Alex", "O Alex escreveu livros", "photo.jpg");
    }

    // -------------------- CREATE --------------------

    @Test
    void createAuthor_success_noPhotoIssue() {
        CreateAuthorRequest request = new CreateAuthorRequest();
        request.setName("Alex");
        request.setBio("O Alex escreveu livros");
        request.setPhoto(null);
        request.setPhotoURI(null);

        when(mapper.create(request)).thenReturn(testAuthor);
        when(authorRepository.save(testAuthor)).thenReturn(testAuthor);

        Author result = authorService.create(request);

        assertNotNull(result);
        assertEquals("Alex", result.getName());
        verify(rabbitMQPublisher, times(1)).publishAuthorCreated(testAuthor);
    }

    @Test
    void createAuthor_photoOnlyURI_setsBothToNull() {
        CreateAuthorRequest request = new CreateAuthorRequest();
        request.setName("Alex");
        request.setBio("Bio");
        request.setPhotoURI("photo.jpg"); // apenas URI

        when(mapper.create(request)).thenReturn(testAuthor);
        when(authorRepository.save(testAuthor)).thenReturn(testAuthor);

        Author result = authorService.create(request);

        assertNull(request.getPhoto());
        assertNull(request.getPhotoURI());
        assertNotNull(result);
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

    // -------------------- PARTIAL UPDATE --------------------

    @Test
    void partialUpdate_success() {
        UpdateAuthorRequest request = new UpdateAuthorRequest();
        request.setName("Alex Updated");

        when(authorRepository.findByAuthorNumber("1")).thenReturn(Optional.of(testAuthor));
        when(authorRepository.save(testAuthor)).thenReturn(testAuthor);

        Author updated = authorService.partialUpdate("1", request, testAuthor.getVersion());
        assertEquals("Alex Updated", updated.getName());
    }

    @Test
    void partialUpdate_authorNotFound_throwsNotFound() {
        UpdateAuthorRequest request = new UpdateAuthorRequest();
        when(authorRepository.findByAuthorNumber("999")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> authorService.partialUpdate("999", request, 0L));
    }

    @Test
    void partialUpdate_photoOnly_setsPhotoNull() {
        UpdateAuthorRequest request = new UpdateAuthorRequest();
        request.setPhotoURI("photo.jpg");
        request.setPhoto(null);

        when(authorRepository.findByAuthorNumber("1")).thenReturn(Optional.of(testAuthor));
        when(authorRepository.save(testAuthor)).thenReturn(testAuthor);

        Author updated = authorService.partialUpdate("1", request, testAuthor.getVersion());
        assertNotNull(updated);
    }

    // -------------------- REMOVE PHOTO --------------------

    @Test
    void removeAuthorPhoto_success() {
        Photo photoMock = mock(Photo.class);
        lenient().when(photoMock.getPhotoFile()).thenReturn("photo.jpg");
        testAuthor.setPhoto("photo.jpg");

        when(authorRepository.findByAuthorNumber("1")).thenReturn(Optional.of(testAuthor));
        when(authorRepository.save(testAuthor)).thenReturn(testAuthor);

        var result = authorService.removeAuthorPhoto("1", testAuthor.getVersion());

        assertTrue(result.isPresent());
        verify(photoRepository, times(1)).deleteByPhotoFile("photo.jpg");
    }

    @Test
    void removeAuthorPhoto_wrongVersion_throwsConflict() {
        when(authorRepository.findByAuthorNumber("1")).thenReturn(Optional.of(testAuthor));

        assertThrows(ConflictException.class,
                () -> authorService.removeAuthorPhoto("1", testAuthor.getVersion() + 1));
    }

    // -------------------- CREATE TEMP AUTHOR --------------------

    @Test
    void createTempAuthor_success() {
        UUID sagaId = UUID.randomUUID();
        OutboxEventMongo tempAuthor = new OutboxEventMongo();
        when(tempAuthorRepository.save(any())).thenReturn(tempAuthor);

        var result = authorService.createTempAuthor("Alex", "Bio", sagaId);

        assertNotNull(result);
        verify(rabbitMQPublisher).publishTempAuthorCreated(argThat(a ->
                a.getName().equals("Alex") && a.getBio().equals("Bio") && a.getSagaId().equals(sagaId)
        ));    }

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
