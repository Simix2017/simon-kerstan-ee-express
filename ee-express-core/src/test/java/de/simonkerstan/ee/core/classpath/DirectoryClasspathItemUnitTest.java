package de.simonkerstan.ee.core.classpath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryClasspathItemUnitTest {

    private final DirectoryClasspathItem tested = new DirectoryClasspathItem(
            Path.of("src/test/resources/test-dir-classpath-item"));

    @Test
    @DisplayName("Test path sanitizer -> Should throw exception")
    void testPathSanitizer() {
        final var exception1 = assertThrows(IllegalArgumentException.class, () -> tested.isResourceExisting("../abc"));
        assertTrue(exception1.getMessage()
                           .contains("must not be outside the base path"));
        final var exception2 = assertThrows(IllegalArgumentException.class, () -> tested.isResourceExisting("/abc"));
        assertTrue(exception2.getMessage()
                           .contains("must be relative"));
    }

    @Test
    @DisplayName("Test isResourceExisting -> Should return true/false")
    void testIsResourceExisting() {
        assertTrue(tested.isResourceExisting("x.txt"));
        assertTrue(tested.isResourceExisting("y"));
        assertFalse(tested.isResourceExisting("z"));
    }

    @Test
    @DisplayName("Test isDirectory -> Should return true/false")
    void testIsDirectory() {
        assertFalse(tested.isDirectory("x.txt"));
        assertTrue(tested.isDirectory("y"));
    }

    @Test
    @DisplayName("Test getChildren -> Should return children")
    void testGetChildren() {
        final var result = tested.getChildren("y");
        assertFalse(result.contains("y"));
        assertTrue(result.contains("xy.txt"));
        assertTrue(result.contains("yy"));
        assertFalse(result.contains("z.txt"));
        assertFalse(result.contains("yy/yz.txt"));
    }

}