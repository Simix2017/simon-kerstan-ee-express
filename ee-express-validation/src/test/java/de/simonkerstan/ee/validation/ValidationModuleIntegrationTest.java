/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.validation;

import de.simonkerstan.ee.core.EeExpressApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationModuleIntegrationTest {

    @Test
    @DisplayName("Test validation module -> Should have configured validator")
    void testValidation() {
        final var configuration = EeExpressApplication.initialize(new String[]{}, "de.simonkerstan.ee.validation.test");
        final var exception = assertThrows(IllegalStateException.class, () -> EeExpressApplication.run(configuration));
        assertTrue(exception.getMessage()
                           .contains("Validation violation!"));
    }

}