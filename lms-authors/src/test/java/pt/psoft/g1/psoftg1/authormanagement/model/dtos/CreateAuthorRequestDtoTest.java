package pt.psoft.g1.psoftg1.authormanagement.model.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateAuthorRequestDtoTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        CreateAuthorRequestDto dto = new CreateAuthorRequestDto(
                "João",
                "Bio do João",
                "photo.jpg",
                "corr-123"
        );

        assertEquals("João", dto.getName());
        assertEquals("Bio do João", dto.getBio());
        assertEquals("photo.jpg", dto.getPhotoURI());
        assertEquals("corr-123", dto.getCorrelationId());
    }

    @Test
    void testSetters() {
        CreateAuthorRequestDto dto = new CreateAuthorRequestDto();

        dto.setName("Maria");
        dto.setBio("Bio da Maria");
        dto.setPhotoURI("photo2.jpg");
        dto.setCorrelationId("corr-456");

        assertEquals("Maria", dto.getName());
        assertEquals("Bio da Maria", dto.getBio());
        assertEquals("photo2.jpg", dto.getPhotoURI());
        assertEquals("corr-456", dto.getCorrelationId());
    }

    @Test
    void testEqualsAndHashCode() {
        CreateAuthorRequestDto dto1 = new CreateAuthorRequestDto("A", "B", "C", "D");
        CreateAuthorRequestDto dto2 = new CreateAuthorRequestDto("A", "B", "C", "D");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CreateAuthorRequestDto dto = new CreateAuthorRequestDto("A", "B", "C", "D");
        String str = dto.toString();

        assertTrue(str.contains("A"));
        assertTrue(str.contains("B"));
        assertTrue(str.contains("C"));
        assertTrue(str.contains("D"));
    }
}
