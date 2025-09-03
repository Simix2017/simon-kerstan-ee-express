package de.simonkerstan.ee.core.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationPropertyResolverUnitTest {

    @Test
    @DisplayName("Resolve an boolean property -> Should return the boolean value")
    void testBoolean() {
        final var tested = new ConfigurationPropertyResolver(_property -> Optional.of("true"));
        assertTrue(tested.resolveConfigurationValue("test.property", Boolean.class)
                           .orElseThrow());
    }

    @Test
    @DisplayName("Resolve an integer property -> Should return the integer value")
    void testInteger() {
        final var tested = new ConfigurationPropertyResolver(_property -> Optional.of("8080"));
        assertEquals(8080, tested.resolveConfigurationValue("test.property", Integer.class)
                .orElseThrow());
    }

    @Test
    @DisplayName("Resolve an string property -> Should return the string value")
    void testString() {
        final var tested = new ConfigurationPropertyResolver(_property -> Optional.of("Hello World!"));
        assertEquals("Hello World!", tested.resolveConfigurationValue("test.property", String.class)
                .orElseThrow());
    }

}