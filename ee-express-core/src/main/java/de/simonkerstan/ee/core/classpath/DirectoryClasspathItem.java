/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.classpath;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Classpath item for a directory.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
@Slf4j
public class DirectoryClasspathItem implements ClasspathItem {

    private final Path path;

    public DirectoryClasspathItem(Path path) {
        this.path = path.toAbsolutePath();
    }

    @Override
    public boolean isResourceExisting(String path) {
        this.sanitizePath(path);
        return Files.exists(this.path.resolve(path));
    }

    @Override
    public boolean isDirectory(String path) {
        this.sanitizePath(path);
        return Files.isDirectory(this.path.resolve(path));
    }

    @Override
    public List<String> getChildren(String path) {
        this.sanitizePath(path);
        final var resolvedPath = this.path.resolve(path);
        try (var walk = Files.walk(resolvedPath, 1)) {
            return walk.filter(other -> !resolvedPath.equals(other))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .toList();
        } catch (IOException e) {
            log.warn("Cannot read children of directory {}", path, e);
            return List.of();
        }
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        this.sanitizePath(path);
        try {
            return Files.newInputStream(this.path.resolve(path));
        } catch (IOException e) {
            log.warn("Cannot read resource from directory {}", path, e);
            return InputStream.nullInputStream();
        }
    }

    @Override
    public void close() throws Exception {
        // Nothing to do.
    }

    private void sanitizePath(String path) {
        final var givenPath = Path.of(path);
        if (givenPath.isAbsolute()) {
            throw new IllegalArgumentException("Path must be relative");
        }

        if (!this.path.resolve(givenPath)
                .normalize()
                .startsWith(this.path)) {
            throw new IllegalArgumentException("Path must not be outside the base path");
        }
    }

}
