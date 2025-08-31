/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.clazz;

import de.simonkerstan.ee.core.classpath.ClasspathResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class RecursivePackageClassIteratorUnitTest {

    @Test
    @DisplayName("Test recursive iteration over a package and its subpackages -> Should go over all classes")
    void testIteration() {
        final var expected = new Class<?>[]{de.simonkerstan.ee.core.test2.Test.class,
                de.simonkerstan.ee.core.test2.sub.SubPackageClass.class};

        final var tested = new RecursivePackageClassIterator("de.simonkersten.ee.core.test2",
                                                             ClasspathResolver.getWrapperForFullClasspath());
        int index = 0;
        while (tested.hasNext()) {
            final var clazz = tested.next();
            assertSame(expected[index], clazz);
            index++;
        }
    }

}