/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di.graph.test;

import de.simonkerstan.ee.core.annotations.MainApplication;
import jakarta.inject.Inject;

/**
 * Test class with two dependencies (one normal and one with a default constructor).
 */
@MainApplication
public class MainClass2 implements Runnable {

    private final TestService testService;
    private final BeanWithDefaultConstructor beanWithDefaultConstructor;

    @Inject
    public MainClass2(TestService testService, BeanWithDefaultConstructor beanWithDefaultConstructor) {
        this.testService = testService;
        this.beanWithDefaultConstructor = beanWithDefaultConstructor;
    }

    @Override
    public void run() {
        // Do nothing
    }

}
