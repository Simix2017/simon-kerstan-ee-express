/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.classpath;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Classpath item for a jar file.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
@RequiredArgsConstructor
@Slf4j
public class JarClasspathItem implements ClasspathItem {

    private final ZipFile jarFile;

    /**
     * Create a new JAR classpath item.
     *
     * @param jarFilePath The path to the jar file to be used
     * @throws IOException If the jar file cannot be opened
     */
    public JarClasspathItem(String jarFilePath) throws IOException {
        final var file = new File(jarFilePath);
        this.jarFile = new ZipFile(file);
    }

    @Override
    public boolean isResourceExisting(String path) {
        return this.getEntry(path)
                .isPresent();
    }

    @Override
    public boolean isDirectory(String path) {
        return this.getEntry(path)
                .map(ZipEntry::isDirectory)
                .orElseThrow(() -> new IllegalArgumentException("Resource is not existing in jar file"));
    }

    @Override
    public List<String> getChildren(String path) {
        final var entry = this.getEntry(path)
                .orElseThrow(() -> new IllegalArgumentException("Resource is not existing in jar file"));
        if (!entry.isDirectory()) {
            throw new IllegalArgumentException("Resource is not a directory");
        }

        return this.jarFile.stream()
                .map(ZipEntry::getName)
                .filter(name -> name.startsWith(path))
                .map(name -> name.substring(path.length() + 1))
                .filter(name -> !name.contains("/"))
                .toList();
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        final var entry = this.getEntry(path)
                .orElseThrow(() -> new IllegalArgumentException("Resource is not existing in jar file"));
        try {
            return this.jarFile.getInputStream(entry);
        } catch (IOException e) {
            log.warn("Could not read resource from jar file", e);
            return InputStream.nullInputStream();
        }
    }

    @Override
    public void close() throws Exception {
        this.jarFile.close();
    }

    private Optional<ZipEntry> getEntry(String path) {
        return Optional.ofNullable(this.jarFile.getEntry(path));
    }

}
