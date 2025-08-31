/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.clazz;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

/**
 * Constructor annotations hook for class scanning.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public interface ConstructorHook {

    /**
     * Get all annotations at the constructor level processed by this hook.
     *
     * @return Constructor annotations processed by this hook
     */
    Class<? extends Annotation>[] getConstructorAnnotations();

    /**
     * Process the given constructor with the given annotation.
     *
     * @param constructor        Constructor to be processed
     * @param annotation         Annotation to be processed
     * @param annotationInstance Annotation instance to be processed
     */
    void processConstructor(Constructor<?> constructor, Class<? extends Annotation> annotation,
                            Annotation annotationInstance);

}
