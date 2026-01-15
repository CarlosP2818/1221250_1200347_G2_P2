package pt.psoft.g1.psoftg1.authormanagement.services.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.repositories.OutboxEventRepository;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorMapper;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.RabbitMQPublisher;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.query.AuthorQueryService;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorCommandServiceImplTest {

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

    @Mock
    AuthorQueryService authorQueryService;

    @InjectMocks
    AuthorCommandServiceImpl authorService;

    Author testAuthor;

    @BeforeEach
    void setup() {
        testAuthor = new Author("Alex", "O Alex escreveu livros", "photo.jpg");
    }


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

    @Test
    void partialUpdate_success() {
        UpdateAuthorRequest request = new UpdateAuthorRequest();
        request.setName("Alex Updated");

        // MOCK correto agora
        when(authorQueryService.findByAuthorNumber("1")).thenReturn(Optional.of(testAuthor));
        when(authorRepository.save(testAuthor)).thenReturn(testAuthor);

        Author updated = authorService.partialUpdate("1", request, testAuthor.getVersion());
        assertEquals("Alex Updated", updated.getName());
    }

    @Test
    void partialUpdate_authorNotFound_throwsNotFound() {
        UpdateAuthorRequest request = new UpdateAuthorRequest();

        // MOCK correto para não encontrar autor
        when(authorQueryService.findByAuthorNumber("999")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> authorService.partialUpdate("999", request, 0L));
    }

    @Test
    void partialUpdate_photoOnly_setsPhotoNull() {
        UpdateAuthorRequest request = new UpdateAuthorRequest();
        request.setPhotoURI("photo.jpg");
        request.setPhoto(null);

        when(authorQueryService.findByAuthorNumber("1")).thenReturn(Optional.of(testAuthor));
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

        when(tempAuthorRepository.save(any(OutboxEventMongo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = authorService.createTempAuthor("Alex", "Bio", sagaId);

        assertNotNull(result);
        assertEquals(sagaId.toString(), result.getCorrelationId());

        verify(rabbitMQPublisher).publishTempAuthorCreated(argThat(a ->
                "Alex".equals(a.getName()) &&
                        "Bio".equals(a.getBio()) &&
                        sagaId.toString().equals(a.getCorrelationId())
        ));
    }

}