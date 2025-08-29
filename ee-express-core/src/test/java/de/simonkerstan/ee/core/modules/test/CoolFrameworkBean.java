/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.modules.test;

import lombok.RequiredArgsConstructor;

/**
 * Cool framework bean used to show that bean providers of framework modules work in integration tests.
 */
@RequiredArgsConstructor
public class CoolFrameworkBean {

    private final String testText;

    /**
     * Cool test method.
     *
     * @return Test string
     */
    public String test() {
        return testText;
    }

}
