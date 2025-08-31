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
import java.util.Arrays;

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
     * Create a new instance of {@link ConstructorBeanCreationInformation} from an existing one.
     *
     * @param creationInformation Existing instance
     * @param singleton           {@code true} if the bean should be a singleton, {@code false} otherwise
     * @return New instance
     */
    public static ConstructorBeanCreationInformation of(ConstructorBeanCreationInformation creationInformation,
                                                        boolean singleton) {
        return new ConstructorBeanCreationInformation(creationInformation.constructor, singleton);
    }

    @Override
    public Class<?>[] getDependencies() {
        return this.constructor.getParameterTypes();
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
        } catch (Exception e) {
            log.debug("Parameter types are {}.", Arrays.asList(this.constructor.getParameterTypes()));
            log.debug("Parameters are {}.", parameters);
            // We catch generic "Exception" because this could be any in an unknown constructor
            throw new BeanInstantiationException("Cannot create bean with type " + this.constructor.getDeclaringClass()
                    .getName(), e);
        }
    }

}
