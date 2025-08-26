/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.clazz;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DistinctPackageTransformerUnitTest {

    @Test
    @DisplayName("Given list of duplicate packages -> Should return distinct list")
    void testDistinctPackageTransformer() {
        final var packages = new String[]{"de.simonkersten.ee.core.test", "de.simonkersten.ee.core.test", "de" +
                ".simonkersten.ee.core.test.sub"};
        final var distinctPackages = DistinctPackageTransformer.distinct(packages);

        assertEquals(1, distinctPackages.length);
        assertEquals("de.simonkersten.ee.core.test", distinctPackages[0]);
    }

}