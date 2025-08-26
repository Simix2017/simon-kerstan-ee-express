/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di.graph;

import de.simonkerstan.ee.core.exceptions.BeanInstantiationException;

/**
 * Necessary information for bean creation.
 */
public interface BeanCreationInformation {

    /**
     * Create a bean instance with the given parameters. This method is allowed to only create a bean instance
     * one-time and reuse it for all following calls.
     *
     * @param parameters Parameters to be used for bean creation
     * @return Bean instance
     * @throws BeanInstantiationException If the bean cannot be instantiated
     */
    Object createBean(Object... parameters) throws BeanInstantiationException;

}
