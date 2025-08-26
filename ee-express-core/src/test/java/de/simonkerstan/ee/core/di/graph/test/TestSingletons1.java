/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di.graph.test;

import jakarta.inject.Inject;
import lombok.Getter;

@Getter
public class TestSingletons1 {

    private final SingletonA singletonA;
    private final SingletonB singletonB;

    @Inject
    public TestSingletons1(SingletonA singletonA, SingletonB singletonB) {
        this.singletonA = singletonA;
        this.singletonB = singletonB;
    }

}
