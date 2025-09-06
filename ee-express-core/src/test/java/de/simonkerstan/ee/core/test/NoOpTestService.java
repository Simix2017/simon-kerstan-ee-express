/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.test;

/**
 * No-op implementation of {@link TestService} used for priority testing.
 */
public class NoOpTestService implements TestService {

    @Override
    public String helloWorld() {
        return "NOTHING TO SEE HERE, IT WILL NOT BE USED!";
    }

}
