/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di.graph;

import de.simonkerstan.ee.core.exceptions.BeanInstantiationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Necessary information for bean creation (when using a constructor).
 */
@Slf4j
@RequiredArgsConstructor
public final class ConstructorBeanCreationInformation implements BeanCreationInformation {

    private final Constructor<?> constructor;
    private final boolean singleton;
    private Object bean;

    /**
     *
     * @param creationInformation
     * @param singleton
     * @return
     */
    public static ConstructorBeanCreationInformation of(ConstructorBeanCreationInformation creationInformation,
                                                        boolean singleton) {
        return new ConstructorBeanCreationInformation(creationInformation.constructor, singleton);
    }

    @Override
    public Object createBean(Object... parameters) {
        if (this.singleton && this.bean != null) {
            // Create singleton beans only once
            return this.bean;
        }

        try {
            this.bean = this.constructor.newInstance(parameters);
            return this.bean;
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new BeanInstantiationException("Cannot create bean with type " + this.constructor.getDeclaringClass()
                    .getName(), e);
        }
    }

}
