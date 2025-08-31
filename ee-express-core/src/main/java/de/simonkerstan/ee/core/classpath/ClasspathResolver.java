/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.classpath;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Resolver for classpath items.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
@Slf4j
public final class ClasspathResolver {

    private ClasspathResolver() {
    }

    /**
     * Get a wrapper for the full classpath.
     *
     * @return Wrapper for the full classpath
     */
    public static ClasspathItem getWrapperForFullClasspath() {
        final var internalClasspathItems = List.of(getJvmClasspathItem());
        final var classpathItems = Arrays.stream(System.getProperty("java.class.path")
                                                         .split(":"))
                .map(ClasspathResolver::fromClasspathEntry)
                .filter(Objects::nonNull)
                .toList();
        final List<ClasspathItem> allClasspathItems = new ArrayList<>(
                internalClasspathItems.size() + classpathItems.size());
        allClasspathItems.addAll(internalClasspathItems);
        allClasspathItems.addAll(classpathItems);
        return new MultipleItemsWrapperClasspathItem(allClasspathItems);
    }

    private static ClasspathItem fromClasspathEntry(String classpathEntry) {
        final var entryPath = Path.of(classpathEntry);
        if (Files.isDirectory(entryPath)) {
            // Directory classpath item
            log.debug("Classpath entry {} is a directory.", classpathEntry);
            return new DirectoryClasspathItem(entryPath);
        } else if (classpathEntry.endsWith(".jar")) {
            // JAR file classpath item
            log.debug("Classpath entry {} is a JAR file.", classpathEntry);
            try {
                return new JarClasspathItem(classpathEntry);
            } catch (IOException e) {
                log.warn("Cannot load JAR file {}.", classpathEntry, e);
                return null;
            }
        }

        // Invalid classpath entry
        log.warn("Invalid classpath entry {}.", classpathEntry);
        return null;
    }

    private static ClasspathItem getJvmClasspathItem() {
        return new JvmClasspathItem();
    }

}
