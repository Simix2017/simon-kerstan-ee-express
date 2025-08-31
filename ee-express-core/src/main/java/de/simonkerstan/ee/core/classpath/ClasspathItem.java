/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.classpath;

import java.io.InputStream;
import java.util.List;

/**
 * One item in the classpath (e.g., a jar file).
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public interface ClasspathItem extends AutoCloseable {

    /**
     * Test whether a resource exists.
     *
     * @param path Path to the resource
     * @return {@code true} if the resource exists, {@code false} otherwise
     */
    boolean isResourceExisting(String path);

    /**
     * Test whether a path points to a directory.
     *
     * @param path Path to the resource
     * @return {@code true} if the path points to a directory, {@code false} otherwise
     */
    boolean isDirectory(String path);

    /**
     * Get the children of a path.
     *
     * @param path Path to the resource
     * @return List of children
     * @throws IllegalArgumentException If the path does not exist or is not a directory
     */
    List<String> getChildren(String path);

    /**
     * Get an input stream for a resource.
     *
     * @param path Path to the resource
     * @return Input stream for the resource
     * @throws IllegalArgumentException If the resource does not exist or is a directory
     */
    InputStream getResourceAsStream(String path);

}
