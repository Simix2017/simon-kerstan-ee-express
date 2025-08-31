package de.simonkerstan.ee.core.classpath;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JarClasspathItemUnitTest {

    private final JarClasspathItem tested;

    JarClasspathItemUnitTest() throws IOException {
        tested = new JarClasspathItem("src/test/resources/test-jar-classpath-item-file.jar");
    }

    @AfterEach
    void cleanUp() throws Exception {
        tested.close();
    }

    @Test
    @DisplayName("Check if resource exists -> Should return true/false")
    void testIsResourceExisting() {
        assertTrue(tested.isResourceExisting("META-INF/MANIFEST.MF"));
        assertFalse(tested.isResourceExisting("non-existing"));
    }

    @Test
    @DisplayName("Check if resource is directory -> Should return true/false")
    void testIsDirectory() {
        assertTrue(tested.isDirectory("META-INF"));
        assertFalse(tested.isDirectory("META-INF/MANIFEST.MF"));
    }

    @Test
    @DisplayName("Get children of a directory -> Should return children")
    void testGetChildren() {
        final var result = tested.getChildren("META-INF");
        assertTrue(result.contains("MANIFEST.MF"));
        assertFalse(result.contains("de-simonkerstan-ee-express/modules"));
        assertFalse(result.contains("modules"));
        assertFalse(result.contains("non-existing"));
    }

}