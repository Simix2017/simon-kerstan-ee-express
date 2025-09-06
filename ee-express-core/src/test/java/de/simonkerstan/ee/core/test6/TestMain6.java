/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.test6;

import de.simonkerstan.ee.core.annotations.MainApplication;
import jakarta.inject.Inject;

/**
 * Main class for testing cyclic dependencies.
 */
@MainApplication
public class TestMain6 implements Runnable {

    private final ServiceA serviceA;
    private final ServiceB serviceB;

    @Inject
    public TestMain6(ServiceA serviceA, ServiceB serviceB) {
        this.serviceA = serviceA;
        this.serviceB = serviceB;
    }

    @Override
    public void run() {
        // Do nothing, as this class is only used for testing cyclic dependencies.
    }

}
