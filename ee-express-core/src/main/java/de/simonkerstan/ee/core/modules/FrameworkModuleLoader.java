/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.modules;

import de.simonkerstan.ee.core.classpath.ClasspathItem;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

/**
 * Loader for framework modules.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
@Slf4j
public final class FrameworkModuleLoader {

    private static final String FRAMEWORK_MODULES_DIRECTORY = "META-INF/de-simonkerstan-ee-express/modules";

    private FrameworkModuleLoader() {
    }

    /**
     * Load all available framework modules.
     *
     * @return List of framework modules
     */
    public static List<FrameworkModule> loadFrameworkModules(ClasspathItem classpathItem) {
        if (!classpathItem.isResourceExisting(FRAMEWORK_MODULES_DIRECTORY)) {
            log.info("No framework modules found at {}.", FRAMEWORK_MODULES_DIRECTORY);
            return List.of();
        }

        return classpathItem.getChildren(FRAMEWORK_MODULES_DIRECTORY)
                .stream()
                .parallel()
                .map(String::trim)
                .peek(resource -> log.debug("Found possible framework module resource {}", resource))
                .filter(FrameworkModuleLoader::isFrameworkModuleResourceEntry)
                .map(resourceEntryName -> getModuleClassName(resourceEntryName, classpathItem))
                .filter(Objects::nonNull)
                .map(FrameworkModuleLoader::instantiateFrameworkModuleClass)
                .filter(Objects::nonNull)
                .toList();
    }

    private static boolean isFrameworkModuleResourceEntry(String resourceEntryName) {
        return resourceEntryName.endsWith(".module");
    }

    private static String getModuleClassName(String resourceEntryName, ClasspathItem classpathItem) {
        try (final var is = classpathItem.getResourceAsStream(FRAMEWORK_MODULES_DIRECTORY + "/" + resourceEntryName)) {
            if (is != null) {
                // Must be the case because the resource exists
                final var reader = new BufferedReader(new InputStreamReader(is));
                return reader.readLine()
                        .trim();
            }

            log.warn("Cannot read resource {}. This should be impossible...", resourceEntryName);
        } catch (IOException e) {
            // Impossible
        }

        return null;
    }

    private static FrameworkModule instantiateFrameworkModuleClass(String className) {
        try {
            final var clazz = Class.forName(className);
            if (!FrameworkModule.class.isAssignableFrom(clazz)) {
                // No framework module class
                return null;
            }

            return (FrameworkModule) clazz.getConstructor()
                    .newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            log.warn("Cannot instantiate framework module class {}", className, e);
            return null;
        }
    }

}
