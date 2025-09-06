/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core;

import de.simonkerstan.ee.core.exceptions.BeanInstantiationException;
import de.simonkerstan.ee.core.test.TestStaticHolder;
import de.simonkerstan.ee.core.test4.Test4;
import de.simonkerstan.ee.core.test5.MainClass5;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EeExpressApplicationIntegrationTest {

    @Test
    @DisplayName("Run a test application in the test package -> Should run the application")
    void testRun() {
        System.setProperty("server.alternative_port", "9090");

        final var applicationConfiguration = EeExpressApplication.initialize(new String[]{"--integration.test=true"},
                                                                             "de.simonkerstan.ee.core.test");
        EeExpressApplication.run(applicationConfiguration);
        // If the following properties are set, the application (and CDI context with all beans) was set up successfully
        assertEquals("Hello World!", TestStaticHolder.getTestProperty());
        assertEquals("CoolFrameworkBean", TestStaticHolder.getTestBeanProviderProperty());

        // Test different configuration sources
        assertTrue(TestStaticHolder.isTestCommandlineConfigurationProperty());
        assertEquals(9090, TestStaticHolder.getTestSystemPropertiesConfigurationProperty());
        assertEquals("Cool ;)", TestStaticHolder.getTestCustomSourceConfigurationProperty());
        assertEquals("Hello World!", TestStaticHolder.getTestEnvironmentConfigurationProperty());
        assertEquals(8080, TestStaticHolder.getTestApplicationPropertiesClasspathConfigurationProperty());
    }

    @Test
    @DisplayName("There is a default constructor bean in the test4 package -> Should run the application")
    void testRunWithDefaultConstructor() {
        final var applicationConfiguration = EeExpressApplication.initialize(new String[]{},
                                                                             "de.simonkerstan.ee.core.test4");
        EeExpressApplication.run(applicationConfiguration);
        // If the following properties are set, the application (and CDI context with all beans) was set up successfully
        assertEquals("Hello World", Test4.getTest());
        assertEquals("Hello World!", Test4.getTest2());
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

    @Test
    @DisplayName("There is no @Inject annotation in the test5 package -> Should run the application")
    void testRunMainClassWithoutInjectAnnotation() {
        final var applicationConfiguration = EeExpressApplication.initialize(new String[]{},
                                                                             "de.simonkerstan.ee.core.test5");
        EeExpressApplication.run(applicationConfiguration);
        // If the following properties are set, the application was started successfully
        assertEquals("true", System.getProperty(MainClass5.class.getName()));
    }

    @Test
    @DisplayName("There are cyclic dependencies in the test6 package -> Should throw exception")
    void testRunWithCyclicDependencies() {
        final var exception = assertThrows(BeanInstantiationException.class,
                                           () -> EeExpressApplication.initialize(new String[]{},
                                                                                 "de.simonkerstan.ee.core.test6"));
        assertTrue(exception.getMessage()
                           .contains("Cyclic dependency detected"));
    }

}