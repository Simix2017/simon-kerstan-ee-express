/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.clazz;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Method annotations hook for class scanning.
 */
public interface MethodHook {

    /**
     * Get all annotations at the method level processed by this hook.
     *
     * @return Method annotations processed by this hook
     */
    Class<? extends Annotation>[] getMethodAnnotations();

    /**
     * Process the given method with the given annotation.
     *
     * @param method             Method to be processed
     * @param annotation         Annotation to be processed
     * @param annotationInstance Annotation instance to be processed
     */
    void processMethod(Method method, Class<? extends Annotation> annotation, Annotation annotationInstance);

}
