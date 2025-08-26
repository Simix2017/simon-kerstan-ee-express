/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.clazz;

import java.lang.annotation.Annotation;

/**
 * Class annotations hook for class scanning.
 */
public interface ClassHook {

    /**
     * Get all annotations at the class level processed by this hook.
     *
     * @return Class annotations processed by this hook
     */
    Class<? extends Annotation>[] getClassAnnotations();

    /**
     * Process the given class with the given annotation.
     *
     * @param clazz              Class to be processed
     * @param annotation         Annotation to be processed
     * @param annotationInstance Annotation instance to be processed
     */
    void processClass(Class<?> clazz, Class<? extends Annotation> annotation, Annotation annotationInstance);

}
