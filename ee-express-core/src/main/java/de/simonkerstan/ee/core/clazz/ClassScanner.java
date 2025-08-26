/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.clazz;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class scanner to find all relevant classes.
 */
@Slf4j
public class ClassScanner {

    @Getter
    private final String[] scanPackages;
    private final Map<Class<? extends Annotation>, List<ClassHook>> classHooks = new HashMap<>();
    private final Map<Class<? extends Annotation>, List<ConstructorHook>> constructorHooks = new HashMap<>();
    private final Map<Class<? extends Annotation>, List<MethodHook>> methodHooks = new HashMap<>();

    /**
     * Create a new class scanner.
     *
     * @param scanPackages Packages to be scanned for classes
     */
    public ClassScanner(String[] scanPackages) {
        this.scanPackages = DistinctPackageTransformer.distinct(scanPackages);
    }

    /**
     * Register a class hook to be called for all classes with the given annotations found.
     *
     * @param hook Class hook to be called
     */
    public void registerClassHook(ClassHook hook) {
        Arrays.stream(hook.getClassAnnotations())
                .forEach(annotation -> classHooks.computeIfAbsent(annotation, key -> new ArrayList<>())
                        .add(hook));
    }

    /**
     * Register a constructor hook to be called for all constructors with the given annotations found.
     *
     * @param hook Constructor hook to be called
     */
    public void registerConstructorHook(ConstructorHook hook) {
        Arrays.stream(hook.getConstructorAnnotations())
                .forEach(annotation -> constructorHooks.computeIfAbsent(annotation, key -> new ArrayList<>())
                        .add(hook));
    }

    /**
     * Register a method hook to be called for all methods with the given annotations found.
     *
     * @param hook Method hook to be called
     */
    public void registerMethodHook(MethodHook hook) {
        Arrays.stream(hook.getMethodAnnotations())
                .forEach(annotation -> methodHooks.computeIfAbsent(annotation, key -> new ArrayList<>())
                        .add(hook));
    }

    /**
     * Scan over all classes and methods and call the registered hooks.
     */
    public void scan() {
        final var hasClassHooks = !this.classHooks.isEmpty();
        final var hasConstructorHooks = !this.constructorHooks.isEmpty();
        final var hasMethodHooks = !this.methodHooks.isEmpty();
        Arrays.stream(this.scanPackages)
                .map(RecursivePackageClassIterator::new)
                .forEach(iterator -> iterator.forEachRemaining(clazz -> {
                    if (hasClassHooks) {
                        // Call class hooks
                        log.debug("Processing annotations of class {}", clazz.getName());
                        final var classAnnotationsWithType = Arrays.stream(clazz.getAnnotations())
                                .collect(Collectors.toUnmodifiableMap(Annotation::annotationType,
                                                                      annotation -> annotation));
                        classAnnotationsWithType.forEach((key, value) -> {
                            Optional.ofNullable(this.classHooks.get(key))
                                    // If there are no hooks for the annotation, return an empty list
                                    .orElse(List.of())
                                    .forEach(hook -> hook.processClass(clazz, key, value));
                        });
                    }

                    if (hasConstructorHooks) {
                        // Call constructor hooks
                        Arrays.stream(clazz.getDeclaredConstructors())
                                .filter(constructor -> constructor.canAccess(null))
                                .forEach(constructor -> {
                                    log.debug("Processing annotations of constructor ({}) of class {}",
                                              Arrays.toString(constructor.getParameters()), clazz.getName());
                                    final var constructorAnnotationsWithType = Arrays.stream(
                                                    constructor.getAnnotations())
                                            .collect(Collectors.toUnmodifiableMap(Annotation::annotationType,
                                                                                  annotation -> annotation));
                                    constructorAnnotationsWithType.forEach((key, value) -> {
                                        Optional.ofNullable(this.constructorHooks.get(key))
                                                // If there are no hooks for the annotation, return an empty list
                                                .orElse(List.of())
                                                .forEach(hook -> hook.processConstructor(constructor, key, value));
                                    });
                                });
                    }

                    if (hasMethodHooks) {
                        // Call method hooks
                        Arrays.stream(clazz.getDeclaredMethods())
                                .forEach(method -> {
                                    // TODO: implement
                                });
                    }
                }));
    }

}
