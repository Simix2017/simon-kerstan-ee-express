/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.web.tomcat.test;

import jakarta.inject.Inject;

/**
 * Test service for dependency-injection testing.
 */
public class TestService {

    @Inject
    public TestService() {
    }

    public String test() {
        return "test";
    }

}
