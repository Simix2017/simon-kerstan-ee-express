/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.test2;

/**
 * This class is used for testing the application bootstrapper but will never run because of the missing annotation.
 */
public class Test implements Runnable {

    @Override
    public void run() {
        System.out.println("Hello World!");
    }

}
