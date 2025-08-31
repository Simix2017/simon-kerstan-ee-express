/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.clazz;

import de.simonkerstan.ee.core.classpath.ClasspathItem;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Recursive package iterator to get all contained classes.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
@Slf4j
final class RecursivePackageClassIterator implements Iterator<Class<?>> {

    private final ClasspathItem classpathItem;
    private final Queue<Class<?>> nextClasses = new LinkedList<>();

    public RecursivePackageClassIterator(String rootPackageName, ClasspathItem classpathItem) {
        this.classpathItem = classpathItem;

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
        if (!this.classpathItem.isResourceExisting(packagePath)) {
            log.info("Package {} does not exist in classpath", packageName);
            return;
        }

        this.classpathItem.getChildren(packagePath)
                .forEach(element -> this.processElement(element, packageName));
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
