/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di.graph.test;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

public class SingletonB {

    @Singleton
    @Inject
    public SingletonB() {
    }

}
