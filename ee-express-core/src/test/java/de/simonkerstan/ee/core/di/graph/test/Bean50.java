/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di.graph.test;

/**
 * Bean with a priority of {@code 50}.
 */
public class Bean50 implements Runnable {

    @Override
    public void run() {
        // Not needed for the unit test
    }

}
