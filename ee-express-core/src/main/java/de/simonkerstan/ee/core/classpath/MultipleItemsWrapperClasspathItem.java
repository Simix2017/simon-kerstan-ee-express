/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.classpath;

import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Multiple items in the classpath addressed as one. All items are visited in the order they are added to this wrapper.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
@RequiredArgsConstructor
public class MultipleItemsWrapperClasspathItem implements ClasspathItem {

    private final List<ClasspathItem> items;

    @Override
    public boolean isResourceExisting(String path) {
        return this.getFirstItemContainingResource(path)
                .isPresent();
    }

    @Override
    public boolean isDirectory(String path) {
        return this.getFirstItemContainingResource(path)
                .map(item -> item.isDirectory(path))
                .orElseThrow(() -> new IllegalArgumentException("Resource is not existing in any classpath item"));
    }

    @Override
    public List<String> getChildren(String path) {
        return this.items.stream()
                .filter(item -> item.isResourceExisting(path))
                .filter(item -> item.isDirectory(path))
                .map(item -> item.getChildren(path))
                .flatMap(List::stream)
                .toList();
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        final var item = this.getFirstItemContainingResource(path);
        if (item.isEmpty()) {
            throw new IllegalArgumentException("Resource is not existing in any classpath item");
        }

        return item.filter(item2 -> !item2.isDirectory(path))
                .map(item2 -> item2.getResourceAsStream(path))
                .orElseThrow(() -> new IllegalArgumentException("Resource is a directory"));
    }

    @Override
    public void close() throws Exception {
        final List<Exception> thrownExceptions = new LinkedList<>();
        for (final var item : this.items) {
            try {
                item.close();
            } catch (Exception e) {
                // Do not throw to close all resources
                thrownExceptions.add(e);
            }
        }

        if (!thrownExceptions.isEmpty()) {
            final var exception = new Exception("Cannot close multiple classpath items");
            thrownExceptions.forEach(exception::addSuppressed);
            throw exception;
        }
    }

    /**
     * Get the first item containing the resource.
     *
     * @param path Path to the resource
     * @return Item containing the resource or empty if none of the items contains the resource
     */
    private Optional<ClasspathItem> getFirstItemContainingResource(String path) {
        for (final var item : this.items) {
            if (item.isResourceExisting(path)) {
                return Optional.of(item);
            }
        }

        return Optional.empty();
    }

}
