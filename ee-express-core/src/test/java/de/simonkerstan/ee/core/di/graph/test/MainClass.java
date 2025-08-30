/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di.graph.test;

import de.simonkerstan.ee.core.annotations.MainApplication;
import jakarta.inject.Inject;

/**
 * Test class with one dependency.
 */
@MainApplication
public class MainClass implements Runnable {

    private final TestService testService;

    @Inject
    public MainClass(TestService testService) {
        this.testService = testService;
    }

    @Override
    public void run() {
        // Do nothing
    }

}
