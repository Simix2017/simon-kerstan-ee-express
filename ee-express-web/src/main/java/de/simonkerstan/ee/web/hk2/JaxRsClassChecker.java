/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.web.hk2;

import jakarta.ws.rs.Path;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * Class to check if a class is a JAX-RS controller class.
 */
final class JaxRsClassChecker {

    private JaxRsClassChecker() {
    }

    /**
     * Check if a class is no JAX-RS controller class.
     *
     * @param beanMapping Bean mapping with class to check
     * @return {@code true} if the class is no JAX-RS controller class, {@code false} otherwise.
     */
    public static boolean isNoJaxRsClass(Map.Entry<Class<?>, Object> beanMapping) {
        final var clazz = beanMapping.getKey();
        final var classAnnotated = Arrays.stream(clazz.getAnnotations())
                // Class annotated with @Path
                .anyMatch(a -> a.annotationType() == Path.class);
        final var anyMethodAnnotated = Arrays.stream(clazz.getDeclaredMethods())
                // Any method annotated with JAX-RS annotations
                .anyMatch(JaxRsClassChecker::isMethodAnnotated);
        final var isJaxRsClass = classAnnotated || anyMethodAnnotated;
        // To prevent Brainfuck ;)
        return !isJaxRsClass;
    }

    private static boolean isMethodAnnotated(Method method) {
        return Arrays.stream(method.getAnnotations())
                // Method annotated with @Path
                .anyMatch(a -> a.annotationType() == Path.class);
    }

}
