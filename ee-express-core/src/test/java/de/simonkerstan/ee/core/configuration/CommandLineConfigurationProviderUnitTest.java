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

class CommandLineConfigurationProviderUnitTest {

    @Test
    @DisplayName("Test command line with equal sign -> Should return the configuration")
    void testEqualOption() {
        final var tested = new CommandLineConfigurationProvider(
                new String[]{"--testCamel=value", "sthElse", "--test_2=xyz"});

        assertEquals(Optional.of("value"), tested.getConfigurationValue("testCamel"));
        assertEquals(Optional.of("xyz"), tested.getConfigurationValue("test_2"));
        assertTrue(tested.getConfigurationValue("sthElse")
                           .isEmpty());
    }

    @Test
    @DisplayName("Test command line with two arguments -> Should return the configuration")
    void testTwoArgumentsOption() {
        final var tested = new CommandLineConfigurationProvider(
                new String[]{"--testCamel", "value", "sthElse", "--test_2", "xyz"});

        assertEquals(Optional.of("value"), tested.getConfigurationValue("testCamel"));
        assertEquals(Optional.of("xyz"), tested.getConfigurationValue("test_2"));
        assertTrue(tested.getConfigurationValue("sthElse")
                           .isEmpty());
    }

}