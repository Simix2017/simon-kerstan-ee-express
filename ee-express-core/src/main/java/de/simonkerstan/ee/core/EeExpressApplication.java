/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core;

import de.simonkerstan.ee.core.bootstrap.MainApplicationHook;
import de.simonkerstan.ee.core.clazz.ClassScanner;
import de.simonkerstan.ee.core.di.DependencyInjectionHook;

/**
 * Main application initializer.
 */
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
        // Scan for classes and methods with annotations
        // This will create beans and contexts (for Jakarta CDI) and register them in the application context
        // Also, the main application class will be registered in the application context
        final var dependencyInjectionHook = new DependencyInjectionHook();
        final var mainApplicationHook = new MainApplicationHook();

        final var classScanner = new ClassScanner(bootstrapPackages);
        // Register dependency injection hooks
        classScanner.registerClassHook(dependencyInjectionHook);
        classScanner.registerConstructorHook(dependencyInjectionHook);
        classScanner.registerMethodHook(dependencyInjectionHook);
        // Register the main application hook
        classScanner.registerClassHook(mainApplicationHook);
        // Scan for classes and methods
        classScanner.scan();

        dependencyInjectionHook.postProcess();
        final var mainClass = mainApplicationHook.getMainApplicationClass();
        final Runnable mainApplication;
        if (mainClass != null) {
            mainApplication = (Runnable) dependencyInjectionHook.getBean(mainClass);
        } else {
            mainApplication = null;
        }

        // Load the configuration and map it to the application context
        // TODO: Load configuration from different sources (e.g. environment variables)

        return new ApplicationContext(null, classScanner.getScanPackages(), dependencyInjectionHook.getBeans(),
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
                                                    + "@MainApplication and has one constructor annotated with " //
                                                    + "@jakarta.inject.Inject?");
        }

        // Run the main application
        applicationContext.getMainApplication()
                .run();
    }

}
