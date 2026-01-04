package pt.psoft.g1.psoftg1.authormanagement.model.dtos;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthorDtoTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        UUID sagaId = UUID.randomUUID();
        AuthorDto dto = new AuthorDto(
                "auth-123",
                "Alex",
                "Bio Teste",
                "photo.jpg",
                sagaId,
                "ACTIVE",
                1L
        );

        assertEquals("auth-123", dto.getAuthorNumber());
        assertEquals("Alex", dto.getName());
        assertEquals("Bio Teste", dto.getBio());
        assertEquals("photo.jpg", dto.getPhotoURI());
        assertEquals(sagaId, dto.getSagaId());
        assertEquals("ACTIVE", dto.getStatus());
        assertEquals(1L, dto.getVersion());
    }

    @Test
    void testSetters() {
        AuthorDto dto = new AuthorDto(null, null, null, null, null, null, 0L);

        UUID sagaId = UUID.randomUUID();

        dto.setAuthorNumber("auth-456");
        dto.setName("Beatriz");
        dto.setBio("Outra Bio");
        dto.setPhotoURI("photo2.jpg");
        dto.setSagaId(sagaId);
        dto.setStatus("INACTIVE");
        dto.setVersion(2L);

        assertEquals("auth-456", dto.getAuthorNumber());
        assertEquals("Beatriz", dto.getName());
        assertEquals("Outra Bio", dto.getBio());
        assertEquals("photo2.jpg", dto.getPhotoURI());
        assertEquals(sagaId, dto.getSagaId());
        assertEquals("INACTIVE", dto.getStatus());
        assertEquals(2L, dto.getVersion());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID sagaId = UUID.randomUUID();
        AuthorDto dto1 = new AuthorDto("auth-789", "Alex", "Bio", "photo.jpg", sagaId, "ACTIVE", 1L);
        AuthorDto dto2 = new AuthorDto("auth-789", "Alex", "Bio", "photo.jpg", sagaId, "ACTIVE", 1L);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UUID sagaId = UUID.randomUUID();
        AuthorDto dto = new AuthorDto("auth-101", "Alex", "Bio", "photo.jpg", sagaId, "ACTIVE", 1L);
        String str = dto.toString();

        assertTrue(str.contains("auth-101"));
        assertTrue(str.contains("Alex"));
        assertTrue(str.contains("Bio"));
        assertTrue(str.contains("photo.jpg"));
        assertTrue(str.contains("ACTIVE"));
    }
}
