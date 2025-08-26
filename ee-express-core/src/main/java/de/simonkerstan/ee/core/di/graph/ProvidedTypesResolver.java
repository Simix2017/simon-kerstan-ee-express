/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di.graph;

import java.io.Closeable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.RandomAccess;
import java.util.Set;

/**
 * Resolver for provided types of a class (all interfaces and superclasses).
 */
final class ProvidedTypesResolver {

    private ProvidedTypesResolver() {
    }

    /**
     * Resolve the provided types of a class.
     *
     * @param clazz Class
     * @return Provided types
     */
    public static Class<?>[] resolve(Class<?> clazz) {
        final Set<Class<?>> providedTypes = new HashSet<>();
        Class<?> superClass = clazz;
        while (superClass != null && superClass != Object.class) {
            // Go through the inheritance hierarchy and add all interfaces

            // Add the superclass
            providedTypes.add(superClass);
            // Add the interfaces
            providedTypes.addAll(Arrays.asList(superClass.getInterfaces()));

            superClass = superClass.getSuperclass();
        }

        // Filter out some types that are not needed
        return providedTypes.stream()
                .filter(providedType -> providedType != Serializable.class && providedType != Cloneable.class && providedType != AutoCloseable.class && providedType != Closeable.class && providedType != RandomAccess.class)
                .toArray(Class[]::new);
    }

}
