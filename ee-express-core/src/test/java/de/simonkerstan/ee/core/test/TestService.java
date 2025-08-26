/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.test;

import jakarta.inject.Inject;

/**
 * Test service for testing the application bootstrapper.
 */
public class TestService {

    @Inject
    public TestService() {
    }

    /**
     * Return a hello-world message for unit testing.
     *
     * @return Hello World!
     */
    public String helloWorld() {
        return "Hello World!";
    }

}
