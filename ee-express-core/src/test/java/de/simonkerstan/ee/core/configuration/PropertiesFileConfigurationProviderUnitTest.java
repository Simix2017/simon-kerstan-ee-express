/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesFileConfigurationProviderUnitTest {

    @Test
    @DisplayName("Get configuration from properties file -> Should return the configuration")
    void testGetConfiguration() throws IOException {
        try (final var propertiesFile = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("test.properties")) {
            if (propertiesFile == null) {
                fail("Properties file not found");
            }

            final var tested = new PropertiesFileConfigurationProvider(propertiesFile);
            assertEquals("Hello World!", tested.getConfigurationValue("cool.example.property")
                    .orElse(""));
            assertTrue(tested.getConfigurationValue("non_existing")
                               .isEmpty());

            final var list = tested.getConfigurationSubValues("cool.example.sub");
            assertArrayEquals(List.of("x", "y")
                                      .toArray(), list.orElseThrow()
                                      .toArray());
        }
    }

}