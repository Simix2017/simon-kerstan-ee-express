/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.clazz;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Recursive package iterator to get all contained classes.
 */
@Slf4j
final class RecursivePackageClassIterator implements Iterator<Class<?>> {

    private final Queue<Class<?>> nextClasses = new LinkedList<>();

    public RecursivePackageClassIterator(String rootPackageName) {
        // Populate the queue with all recursive subpackages
        this.processPackage(rootPackageName);
    }

    @Override
    public boolean hasNext() {
        return !this.nextClasses.isEmpty();
    }

    @Override
    public Class<?> next() {
        return this.nextClasses.poll();
    }

    private void processPackage(String packageName) {
        final var packagePath = packageName.replace('.', '/');
        try (final var is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(packagePath)) {
            if (is == null) {
                log.warn("Cannot find package {}", packageName);
                return;
            }

            // Iterate over all elements in the package
            final var reader = new BufferedReader(new InputStreamReader(is));
            reader.lines()
                    .forEach(elementName -> processElement(elementName, packageName));
        } catch (IOException e) {
            log.warn("Cannot read package {}", packageName, e);
        }
    }

    private void processElement(String elementName, String packageName) {
        if (elementName.endsWith(".class")) {
            // Class file
            final var className = elementName.substring(0, elementName.length() - 6);
            final var classPath = packageName + "." + className;
            try {
                // Add the class to the queue
                final var clazz = Class.forName(classPath);
                this.nextClasses.add(clazz);
            } catch (ClassNotFoundException e) {
                log.warn("Cannot find class {}", classPath, e);
            }
        } else {
            // Subpackage
            final var subPackagePath = packageName + "." + elementName;
            this.processPackage(subPackagePath);
        }
    }

}
