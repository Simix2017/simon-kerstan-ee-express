/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SystemPropertiesConfigurationProviderUnitTest {

    private final SystemPropertiesConfigurationProvider tested = new SystemPropertiesConfigurationProvider();

    @Test
    @DisplayName("Get property from system properties -> Should return the property")
    void testGetProperty() {
        System.setProperty("test.property", "testValue");

        assertEquals(Optional.of("testValue"), tested.getConfigurationValue("test.property"));
        assertTrue(tested.getConfigurationValue("non_existing")
                           .isEmpty());
    }

}