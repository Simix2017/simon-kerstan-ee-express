/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.test5;

import de.simonkerstan.ee.core.annotations.MainApplication;

/**
 * Main class for testing the application bootstrapper (not annotated with {@link jakarta.inject.Inject}).
 */
@MainApplication
public class MainClass5 implements Runnable {

    @Override
    public void run() {
        System.setProperty(MainClass5.class.getName(), "true");
    }

}
