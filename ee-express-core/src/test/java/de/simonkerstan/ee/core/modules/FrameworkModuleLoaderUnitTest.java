package de.simonkerstan.ee.core.modules;

import de.simonkerstan.ee.core.CoreModule;
import de.simonkerstan.ee.core.classpath.ClasspathResolver;
import de.simonkerstan.ee.core.modules.test.TestModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FrameworkModuleLoaderUnitTest {

    @Test
    @DisplayName("Load all modules -> Should load all modules")
    void testLoadModules() {
        final var modules = FrameworkModuleLoader.loadFrameworkModules(ClasspathResolver.getWrapperForFullClasspath());
        assertEquals(2, modules.size());

        // Assert TestModule and CoreModule is loaded (0 = core module; 1 = test module)
        final var coreModule = modules.get(0);
        assertSame(CoreModule.class, coreModule.getClass());
        final var testModule = modules.get(1);
        assertSame(TestModule.class, testModule.getClass());
    }

}