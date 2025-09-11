/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import de.simonkerstan.ee.core.configuration.types.TestEnum;
import de.simonkerstan.ee.core.configuration.types.TestRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigurationPropertyResolverUnitTest {

    @Test
    @DisplayName("Resolve a record property -> Should return a record instance")
    void testRecord() {
        final var tested = new ConfigurationPropertyResolver(property -> {
            if ("test.helloWorld".equals(property)) {
                return Optional.of("Hello World!");
            } else if ("test.port".equals(property)) {
                return Optional.of("8080");
            }

            return Optional.empty();
        });
        assertEquals(new TestRecord("Hello World!", 8080), tested.resolveConfigurationValue("test", TestRecord.class)
                .orElseThrow());
    }

    @Test
    @DisplayName("Resolve an enum property -> Should return an enum value")
    void testEnum() {
        final var tested = new ConfigurationPropertyResolver(_property -> Optional.of("X"));
        assertEquals(TestEnum.X, tested.resolveConfigurationValue("test.property", TestEnum.class)
                .orElseThrow());
    }

    @Test
    @DisplayName("Resolve a string property -> Should return the string value")
    void testString() {
        final var tested = new ConfigurationPropertyResolver(_property -> Optional.of("Hello World!"));
        assertEquals("Hello World!", tested.resolveConfigurationValue("test.property", String.class)
                .orElseThrow());
    }

    @Test
    @DisplayName("Resolve a character property -> Should return the character value")
    void testCharacter() {
        final var tested = new ConfigurationPropertyResolver(_property -> Optional.of("x"));
        assertEquals('x', tested.resolveConfigurationValue("test.property", Character.class)
                .orElseThrow());
    }

    @Test
    @DisplayName("Resolve a boolean property -> Should return the boolean value")
    void testBoolean() {
        final var tested = new ConfigurationPropertyResolver(_property -> Optional.of("true"));
        assertTrue(tested.resolveConfigurationValue("test.property", Boolean.class)
                           .orElseThrow());
    }

    @Test
    @DisplayName("Resolve a byte property -> Should return the byte value")
    void testByte() {
        final var tested = new ConfigurationPropertyResolver(_property -> Optional.of("8"));
        assertEquals((byte) 8, tested.resolveConfigurationValue("test.property", Byte.class)
                .orElseThrow());
    }

    @Test
    @DisplayName("Resolve a short property -> Should return the short value")
    void testShort() {
        final var tested = new ConfigurationPropertyResolver(_property -> Optional.of("8080"));
        assertEquals((short) 8080, tested.resolveConfigurationValue("test.property", Short.class)
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
    @DisplayName("Resolve a long property -> Should return the long value")
    void testLong() {
        final var tested = new ConfigurationPropertyResolver(_property -> Optional.of("8080"));
        assertEquals(8080L, tested.resolveConfigurationValue("test.property", Long.class)
                .orElseThrow());
    }

    @Test
    @DisplayName("Resolve a float property -> Should return the float value")
    void testFloat() {
        final var tested = new ConfigurationPropertyResolver(_property -> Optional.of("8.12"));
        assertEquals(8.12f, tested.resolveConfigurationValue("test.property", Float.class)
                .orElseThrow());
    }

    @Test
    @DisplayName("Resolve a double property -> Should return the double value")
    void testDouble() {
        final var tested = new ConfigurationPropertyResolver(_property -> Optional.of("8.33"));
        assertEquals(8.33, tested.resolveConfigurationValue("test.property", Double.class)
                .orElseThrow());
    }

}