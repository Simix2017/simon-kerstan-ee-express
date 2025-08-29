/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di.graph;

import de.simonkerstan.ee.core.exceptions.BeanInstantiationException;
import lombok.RequiredArgsConstructor;

/**
 * Bean creation information for beans that are not instantiated but provided directly.
 */
@RequiredArgsConstructor
public class ObjectBeanCreationInformation implements BeanCreationInformation {

    private final Object bean;

    @Override
    public Object createBean(Object... parameters) throws BeanInstantiationException {
        return this.bean;
    }

}
