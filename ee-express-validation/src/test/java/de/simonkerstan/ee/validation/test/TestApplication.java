/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.validation.test;

import de.simonkerstan.ee.core.annotations.MainApplication;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;

/**
 * Test application class for validation module integration tests.
 */
@MainApplication
@Slf4j
public class TestApplication implements Runnable {

    private final Validator validator;

    @Inject
    public TestApplication(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void run() {
        final var testObject = new TestObject("");
        final var violations = this.validator.validate(testObject);
        violations.forEach(violation -> log.error("Violation (expected): {}", violation.getMessage()));
        if (!violations.isEmpty()) {
            throw new IllegalStateException("Validation violation!");
        }
    }

    private class TestObject {

        @NotBlank
        private final String xyz;

        public TestObject(@NotBlank String xyz) {
            this.xyz = xyz;
        }

    }

}
