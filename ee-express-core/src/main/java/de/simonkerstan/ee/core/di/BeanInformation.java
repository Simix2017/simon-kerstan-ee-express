/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di;

import de.simonkerstan.ee.core.di.graph.BeanCreationInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Information about a bean to be used to create a new instance.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class BeanInformation {

    private int priority = 0;
    private boolean singleton = false;
    private List<Class<?>> dependencies;
    private BeanCreationInformation creationInformation;

}
