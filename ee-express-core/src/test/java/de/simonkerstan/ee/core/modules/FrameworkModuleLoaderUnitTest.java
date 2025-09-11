/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.modules;

import de.simonkerstan.ee.core.CoreModule;
import de.simonkerstan.ee.core.classpath.ClasspathResolver;
import de.simonkerstan.ee.core.modules.test.AfterModule;
import de.simonkerstan.ee.core.modules.test.TestModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class FrameworkModuleLoaderUnitTest {

    @Test
    @DisplayName("Load all modules -> Should load all modules")
    void testLoadModules() {
        final var modules = FrameworkModuleLoader.loadFrameworkModules(ClasspathResolver.getWrapperForFullClasspath());
        assertEquals(3, modules.size());

        // Assert TestModule and CoreModule is loaded (0 = core module; 1 = test module; 2 = after module)
        final var coreModule = modules.get(0);
        assertSame(CoreModule.class, coreModule.getClass());
        final var testModule = modules.get(1);
        assertSame(TestModule.class, testModule.getClass());
        final var afterModule = modules.get(2);
        assertSame(AfterModule.class, afterModule.getClass());
    }

}