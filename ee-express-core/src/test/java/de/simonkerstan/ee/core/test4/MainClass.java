/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.test4;

import de.simonkerstan.ee.core.annotations.MainApplication;
import jakarta.inject.Inject;

/**
 * Main class to test default constructors (integration test).
 */
@MainApplication
public class MainClass implements Runnable {

    private final TestInterface testInterface;
    private final TestComponent testComponent;

    @Inject
    public MainClass(TestInterface testInterface, TestComponent testComponent) {
        this.testInterface = testInterface;
        this.testComponent = testComponent;
    }

    @Override
    public void run() {
        Test4.setTest(this.testInterface.getSomething());
        Test4.setTest2(this.testComponent.getSomething());
    }

}
