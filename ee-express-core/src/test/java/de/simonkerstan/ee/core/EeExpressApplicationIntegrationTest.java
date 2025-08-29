/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core;

import de.simonkerstan.ee.core.exceptions.BeanInstantiationException;
import de.simonkerstan.ee.core.test.TestStaticHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EeExpressApplicationIntegrationTest {

    @Test
    @DisplayName("Run a test application in the test package -> Should run the application")
    void testRun() {
        final var applicationConfiguration = EeExpressApplication.initialize(new String[]{},
                                                                             "de.simonkerstan.ee.core.test");
        EeExpressApplication.run(applicationConfiguration);
        // If the following properties are set, the application (and CDI context with all beans) was set up successfully
        assertEquals("Hello World!", TestStaticHolder.getTestProperty());
        assertEquals("CoolFrameworkBean", TestStaticHolder.getTestBeanProviderProperty());
    }

    @Test
    @DisplayName("Run a non-existing test application in the test2 package -> Should throw exception")
    void testRunWithoutMainApplicationClass() {
        final var applicationConfiguration = EeExpressApplication.initialize(new String[]{},
                                                                             "de.simonkerstan.ee.core.test2");
        final var exception = assertThrows(IllegalStateException.class,
                                           () -> EeExpressApplication.run(applicationConfiguration));
        assertTrue(exception.getMessage()
                           .contains("No main application class found or instantiation failed"));
    }

    @Test
    @DisplayName("There are unsatisfied dependencies in the test3 package -> Should throw exception")
    void testRunWithUnsatisfiedDependencies() {
        final var exception = assertThrows(BeanInstantiationException.class,
                                           () -> EeExpressApplication.initialize(new String[]{},
                                                                                 "de.simonkerstan.ee.core.test3"));
        assertTrue(exception.getMessage()
                           .contains("unresolvable dependencies"));
    }

}