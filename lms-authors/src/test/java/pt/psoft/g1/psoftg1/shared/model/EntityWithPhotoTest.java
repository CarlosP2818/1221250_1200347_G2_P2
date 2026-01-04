package pt.psoft.g1.psoftg1.shared.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityWithPhotoTest {

    // Classe concreta para testes
    static class EntityWithPhotoImpl extends EntityWithPhoto {}

    @Test
    void testSetPhotoWithValidPath() {
        EntityWithPhotoImpl entity = new EntityWithPhotoImpl();
        String validPath = "photo.jpg";

        entity.setPhoto(validPath);

        assertNotNull(entity.getPhoto());
        assertEquals(validPath, entity.getPhoto().getPhotoFile());
    }

    @Test
    void testSetPhotoWithNull() {
        EntityWithPhotoImpl entity = new EntityWithPhotoImpl();

        entity.setPhoto(null);

        assertNull(entity.getPhoto());
    }

    @Test
    void testSetPhotoWithInvalidPath() {
        EntityWithPhotoImpl entity = new EntityWithPhotoImpl();
        String invalidPath = "\0invalidpath"; // caracteres inválidos em paths

        entity.setPhoto(invalidPath);

        assertNull(entity.getPhoto(), "Invalid path should result in null photo");
    }

    @Test
    void testSetPhotoInternalDirectly() {
        EntityWithPhotoImpl entity = new EntityWithPhotoImpl();

        // Testa comportamento interno com null
        entity.setPhotoInternal(null);
        assertNull(entity.getPhoto());

        // Testa comportamento interno com caminho válido
        entity.setPhotoInternal("photo2.jpg");
        assertNotNull(entity.getPhoto());
        assertEquals("photo2.jpg", entity.getPhoto().getPhotoFile());
    }
}
