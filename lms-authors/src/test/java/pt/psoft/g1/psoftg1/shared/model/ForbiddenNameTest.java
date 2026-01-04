package pt.psoft.g1.psoftg1.shared.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForbiddenNameTest {

    @Test
    void testConstructorAndGetter() {
        ForbiddenName fn = new ForbiddenName("Admin");

        assertNotNull(fn);
        assertEquals("Admin", fn.getForbiddenName());
    }

    @Test
    void testSetter() {
        ForbiddenName fn = new ForbiddenName();
        fn.setForbiddenName("Root");

        assertEquals("Root", fn.getForbiddenName());
    }

    @Test
    void testConstructorWithNull() {
        ForbiddenName fn = new ForbiddenName(null);
        assertNull(fn.getForbiddenName()); // Constructor allows null, setter does too
    }

    @Test
    void testConstructorWithEmptyString() {
        ForbiddenName fn = new ForbiddenName("");
        assertEquals("", fn.getForbiddenName());
    }

    @Test
    void testSetterWithEmptyString() {
        ForbiddenName fn = new ForbiddenName();
        fn.setForbiddenName("");
        assertEquals("", fn.getForbiddenName());
    }
}
