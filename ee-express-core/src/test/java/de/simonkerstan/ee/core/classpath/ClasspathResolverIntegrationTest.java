/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.classpath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClasspathResolverIntegrationTest {

    @Test
    @DisplayName("Load resource from classpath -> Should load the resource")
    void testLoadResource() {
        final var classpathItem = ClasspathResolver.getWrapperForFullClasspath();
        assertNotNull(classpathItem);
        assertTrue(classpathItem.isResourceExisting("java/lang/Object.class"));
        assertTrue(classpathItem.isResourceExisting(
                "de/simonkerstan/ee/core/classpath/ClasspathResolverIntegrationTest.class"));
        assertFalse(classpathItem.isResourceExisting("non-existing-xyz-123"));
        // We would like to check the following, but the JVM does not provide enough information...
        //assertTrue(classpathItem.isDirectory("java/lang"));
    }

}