/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core;

import de.simonkerstan.ee.core.bootstrap.MainApplicationHook;
import de.simonkerstan.ee.core.classpath.ClasspathResolver;
import de.simonkerstan.ee.core.clazz.ClassScanner;
import de.simonkerstan.ee.core.configuration.DefaultConfiguration;
import de.simonkerstan.ee.core.di.DependencyInjectionHook;
import de.simonkerstan.ee.core.modules.FrameworkModuleLoader;
import lombok.extern.slf4j.Slf4j;

/**
 * Main application initializer.
 */
@Slf4j
public final class EeExpressApplication {

    private EeExpressApplication() {
    }

    /**
     * Initialize the application.
     *
     * @param args              Command line arguments
     * @param bootstrapPackages Packages to be scanned by all Jakarta EE components
     * @return Application context
     */
    public static ApplicationContext initialize(String[] args, String... bootstrapPackages) {
        // Scan for classes and methods with annotations.
        // This will create beans and contexts (for Jakarta CDI) and register them in the application context.
        // Also, the main application class will be registered in the application context.
        // In addition to that, also all framework modules will be loaded, initialized and registered.

        // Get a wrapper for the full classpath
        final var classpathItem = ClasspathResolver.getWrapperForFullClasspath();

        // Load all framework modules
        final var modules = FrameworkModuleLoader.loadFrameworkModules(classpathItem);
        log.info("Loaded {} framework module(s)", modules.size());
        if (log.isDebugEnabled()) {
            modules.forEach(module -> log.debug("Loaded framework module {}", module.getClass()
                    .getName()));
        }

        // Prepare all framework hooks
        final var dependencyInjectionHook = new DependencyInjectionHook();
        final var mainApplicationHook = new MainApplicationHook();

        // Load the configuration and map it to the application context
        final var configuration = new DefaultConfiguration(args);

        // Scan all base packages for dependency injection and module initialization
        final var classScanner = new ClassScanner(bootstrapPackages, classpathItem);
        // Register dependency injection hooks
        classScanner.registerClassHook(dependencyInjectionHook);
        classScanner.registerClassInterfacesHook(dependencyInjectionHook);
        classScanner.registerConstructorHook(dependencyInjectionHook);
        classScanner.registerMethodHook(dependencyInjectionHook);
        // Register the main application hook
        classScanner.registerClassHook(mainApplicationHook);
        // Register all module hooks
        modules.forEach(module -> module.classHooks()
                .forEach(classScanner::registerClassHook));
        modules.forEach(module -> module.constructorHooks()
                .forEach(classScanner::registerConstructorHook));
        modules.forEach(module -> module.methodHooks()
                .forEach(classScanner::registerMethodHook));
        // Scan for classes and methods
        classScanner.scan();

        // Initialize all framework modules (already sorted by priority)
        modules.forEach(module -> {
            module.init(configuration, classpathItem, dependencyInjectionHook);
            module.beanProviders()
                    .forEach(dependencyInjectionHook::addBeanProvider);
        });

        // Create all beans and set up the CDI context
        dependencyInjectionHook.postProcess();

        // Get the main application class and save it in the application context
        final var mainApplication = getMainApplication(mainApplicationHook, dependencyInjectionHook);

        // Create the application context
        return new ApplicationContext(configuration, classScanner.getScanPackages(), dependencyInjectionHook.getBeans(),
                                      mainApplication);
    }

    /**
     * Run the application.
     *
     * @param applicationContext Application context
     */
    public static void run(ApplicationContext applicationContext) {
        if (applicationContext.getMainApplication() == null) {
            throw new IllegalStateException("No main application class found or instantiation failed. " //
                                                    + "Is the main application class annotated with " //
                                                    + "@MainApplication and has either one constructor annotated " //
                                                    + "with @jakarta.inject.Inject or an accessible default " //
                                                    + "constructor?");
        }

        // Run the main application
        applicationContext.getMainApplication()
                .run();
    }

    private static Runnable getMainApplication(MainApplicationHook mainApplicationHook,
                                               DependencyInjectionHook dependencyInjectionHook) {
        final var mainClass = mainApplicationHook.getMainApplicationClass();
        Runnable mainApplication;
        if (mainClass != null) {
            // Main application class found, now we try to find an instantiated object or created one
            mainApplication = (Runnable) dependencyInjectionHook.getBean(mainClass);
            if (mainApplication == null) {
                // Not instantiated yet, try to create one
                try {
                    mainApplication = (Runnable) mainClass.getDeclaredConstructor()
                            .newInstance();
                } catch (Exception e) {
                    log.error("Cannot instantiate main application class {}", mainClass.getName(), e);
                }
            }
        } else {
            // No main application class found
            mainApplication = null;
        }

        return mainApplication;
    }

}
