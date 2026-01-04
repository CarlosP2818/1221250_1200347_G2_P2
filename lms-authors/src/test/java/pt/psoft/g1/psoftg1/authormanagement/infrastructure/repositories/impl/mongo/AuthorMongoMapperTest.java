package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.mongo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.persistence.mongo.AuthorMongo;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.shared.services.IdGenerators.IdGenerator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorMongoMapperTest {

    private IdGenerator idGenerator;
    private AuthorMongoMapper mapper;

    @BeforeEach
    void setUp() {
        idGenerator = mock(IdGenerator.class);
        mapper = new AuthorMongoMapper(idGenerator);
    }

    // ------------------- toDomain -------------------

    @Test
    void toDomain_withNull_returnsNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toDomain_mapsAllFieldsCorrectly() {
        AuthorMongo mongo = new AuthorMongo();
        mongo.setAuthorNumber("123");
        mongo.setName("João");
        mongo.setBio("Bio do João");
        mongo.setPhoto("photo.jpg");

        Author author = mapper.toDomain(mongo);

        assertNotNull(author);
        assertEquals("123", author.getId());
        assertEquals("João", author.getName());
        assertEquals("Bio do João", author.getBio());
        assertNotNull(author.getPhoto());
        assertEquals("photo.jpg", author.getPhoto().getPhotoFile());
    }

    // ------------------- toMongo -------------------

    @Test
    void toMongo_withNull_returnsNull() {
        assertNull(mapper.toMongo(null));
    }

    @Test
    void toMongo_mapsAllFieldsCorrectly() {
        when(idGenerator.generateId()).thenReturn("generatedId");

        Author author = new Author("Maria", "Bio da Maria", "photo.jpg");
        author.setAuthorNumber("existingId");

        AuthorMongo mongo = mapper.toMongo(author);

        assertNotNull(mongo);
        assertEquals("generatedId", mongo.getId()); // gerado pelo idGenerator
        assertEquals("existingId", mongo.getAuthorNumber());
        assertEquals(0L, mongo.getVersion());
        assertEquals("Maria", mongo.getName());
        assertEquals("Bio da Maria", mongo.getBio());
        assertEquals("photo.jpg", mongo.getPhoto());
    }

    @Test
    void toMongo_withNullPhoto_setsPhotoNull() {
        when(idGenerator.generateId()).thenReturn("generatedId");

        Author author = new Author("Carlos", "Bio do Carlos", null);
        author.setAuthorNumber("existingId");

        AuthorMongo mongo = mapper.toMongo(author);

        assertNotNull(mongo);
        assertNull(mongo.getPhoto());
    }
}
