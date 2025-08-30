/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core;

import de.simonkerstan.ee.core.bootstrap.MainApplicationHook;
import de.simonkerstan.ee.core.clazz.ClassScanner;
import de.simonkerstan.ee.core.configuration.Configuration;
import de.simonkerstan.ee.core.configuration.DefaultConfiguration;
import de.simonkerstan.ee.core.di.BeanProvider;
import de.simonkerstan.ee.core.di.DependencyInjectionHook;
import de.simonkerstan.ee.core.modules.FrameworkModule;
import de.simonkerstan.ee.core.modules.FrameworkModuleLoader;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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

        // Load all framework modules
        final var modules = FrameworkModuleLoader.loadFrameworkModules();
        log.info("Loaded {} framework modules", modules.size());
        if (log.isDebugEnabled()) {
            modules.forEach(module -> log.debug("Loaded framework module {}", module.getClass()
                    .getName()));
        }

        // Prepare all framework hooks
        final var dependencyInjectionHook = new DependencyInjectionHook();
        final var mainApplicationHook = new MainApplicationHook();

        // Load the configuration and map it to the application context
        // TODO: Load configuration from different sources (e.g. environment variables)
        final var configuration = new DefaultConfiguration();
        dependencyInjectionHook.addBeanProvider(new BeanProvider<>(Configuration.class, configuration, 0));

        // Scan all base packages for dependency injection and module initialization
        final var classScanner = new ClassScanner(bootstrapPackages);
        // Register dependency injection hooks
        classScanner.registerClassHook(dependencyInjectionHook);
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

        // Initialize all framework modules
        modules.forEach(FrameworkModule::init);

        // Create all beans and set up the CDI context
        modules.stream()
                .map(FrameworkModule::beanProviders)
                .flatMap(List::stream)
                .forEach(dependencyInjectionHook::addBeanProvider);
        dependencyInjectionHook.postProcess();

        // Get the main application class and save it in the application context
        final var mainClass = mainApplicationHook.getMainApplicationClass();
        final Runnable mainApplication;
        if (mainClass != null) {
            mainApplication = (Runnable) dependencyInjectionHook.getBean(mainClass);
        } else {
            mainApplication = null;
        }

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
                                                    + "@MainApplication and has one constructor annotated with " //
                                                    + "@jakarta.inject.Inject?");
        }

        // Run the main application
        applicationContext.getMainApplication()
                .run();
    }

}
