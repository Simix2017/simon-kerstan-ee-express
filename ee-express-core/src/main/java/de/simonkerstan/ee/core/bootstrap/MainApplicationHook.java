/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.bootstrap;

import de.simonkerstan.ee.core.annotations.MainApplication;
import de.simonkerstan.ee.core.clazz.ClassHook;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Hook to find the main application class.
 */
@Slf4j
public class MainApplicationHook implements ClassHook {

    private int mainApplicationClassCount = 0;
    private Class<?> mainApplicationClass;

    @Override
    public Class<? extends Annotation>[] getClassAnnotations() {
        return new Class[]{MainApplication.class};
    }

    @Override
    public void processClass(Class<?> clazz, Class<? extends Annotation> annotation, Annotation annotationInstance) {
        // This hook is only registered for one annotation, so we can ignore the annotation parameter
        if (Arrays.stream(clazz.getInterfaces())
                .anyMatch(clazzInterface -> clazzInterface == Runnable.class)) {
            log.debug("Found possible main application class {}", clazz.getName());
            this.mainApplicationClassCount++;
            this.mainApplicationClass = clazz;
        }
    }

    /**
     * Get the main application class. If there are multiple classes annotated with {@link MainApplication}, this
     * method will also return {@code null}.
     *
     * @return Main application class or {@code null} if not found
     */
    public Class<?> getMainApplicationClass() {
        return this.mainApplicationClassCount == 1 ? this.mainApplicationClass : null;
    }

}
