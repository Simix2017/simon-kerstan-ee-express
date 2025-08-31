/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.classpath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Classpath item for JVM internal classes and resources.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public class JvmClasspathItem implements ClasspathItem {

    private final ClassLoader jvmClassLoader = ClassLoader.getPlatformClassLoader();

    @Override
    public boolean isResourceExisting(String path) {
        return jvmClassLoader.getResource(path) != null;
    }

    @Override
    public boolean isDirectory(String path) {
        try (final var is = jvmClassLoader.getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalArgumentException("Resource is not existing in JVM classpath");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Resource is not existing in JVM classpath", e);
        }

        // We can never distinguish between a directory and a file
        return false;
    }

    @Override
    public List<String> getChildren(String path) {
        try (final var is = jvmClassLoader.getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalArgumentException("Resource is not existing in JVM classpath");
            }

            // Iterate over all elements in the directory
            // This is the best effort because we cannot say, the given path is a directory or not
            final var reader = new BufferedReader(new InputStreamReader(is));
            return reader.lines()
                    .toList();
        } catch (IOException e) {
            throw new IllegalArgumentException("Resource is not existing in JVM classpath", e);
        }
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        final var stream = jvmClassLoader.getResourceAsStream(path);
        if (stream == null) {
            throw new IllegalArgumentException("Resource is not existing in JVM classpath");
        }
        return stream;
    }

    @Override
    public void close() throws Exception {

    }

}
