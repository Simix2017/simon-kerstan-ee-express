/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.modules;

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

    private FrameworkModuleLoader() {
    }

    /**
     * Load all available framework modules.
     *
     * @return List of framework modules
     */
    public static List<FrameworkModule> loadFrameworkModules() {
        try (final var is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("de-simonkerstan-ee-express")) {
            if (is == null) {
                throw new IOException("Cannot find framework modules");
            }

            final var reader = new BufferedReader(new InputStreamReader(is));
            return reader.lines()
                    .parallel()
                    .map(String::trim)
                    .filter(FrameworkModuleLoader::isFrameworkModuleResourceEntry)
                    .map(FrameworkModuleLoader::getModuleClassName)
                    .filter(Objects::nonNull)
                    .map(FrameworkModuleLoader::instantiateFrameworkModuleClass)
                    .filter(Objects::nonNull)
                    .toList();
        } catch (IOException e) {
            log.warn("Cannot load framework modules", e);
            return List.of();
        }
    }

    private static boolean isFrameworkModuleResourceEntry(String resourceEntryName) {
        return resourceEntryName.endsWith(".module");
    }

    private static String getModuleClassName(String resourceEntryName) {
        try (final var is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("de-simonkerstan-ee-express/" + resourceEntryName)) {
            if (is != null) {
                // Must be the case because the resource exists
                final var reader = new BufferedReader(new InputStreamReader(is));
                return reader.readLine()
                        .trim();
            }
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
