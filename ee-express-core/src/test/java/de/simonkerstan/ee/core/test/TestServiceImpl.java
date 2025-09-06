/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.test;

import de.simonkerstan.ee.core.annotations.BeanPriority;
import jakarta.inject.Inject;

/**
 * Test service for testing the application bootstrapper.
 */
@BeanPriority(10)
public class TestServiceImpl implements TestService {

    @Inject
    public TestServiceImpl() {
    }

    @Override
    public String helloWorld() {
        return "Hello World!";
    }

}
