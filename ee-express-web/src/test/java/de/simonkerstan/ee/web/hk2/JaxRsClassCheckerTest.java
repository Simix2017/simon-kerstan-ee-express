/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.web.hk2;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JaxRsClassCheckerTest {

    @Test
    @DisplayName("Test JAX-RS controller class checks")
    void testJaxRsClassChecker() {
        assertTrue(JaxRsClassChecker.isNoJaxRsClass(Map.entry(NoJaxRsClass.class, new NoJaxRsClass())));
        assertFalse(JaxRsClassChecker.isNoJaxRsClass(Map.entry(Controller1.class, new Controller1())));
        assertFalse(JaxRsClassChecker.isNoJaxRsClass(Map.entry(Controller2.class, new Controller2())));
    }

    /**
     * Test class for {@link JaxRsClassChecker} (no JAX-RS annotations).
     */
    private static class NoJaxRsClass {
    }

    /**
     * Test controller class for {@link JaxRsClassChecker} (only method annotated).
     */
    private static class Controller1 {

        @GET
        @Path("/test")
        public String test() {
            return "test";
        }

    }

    /**
     * Test controller class for {@link JaxRsClassChecker} (class and method annotated).
     */
    @Path("/test")
    private static class Controller2 {

        @GET
        @Path("/test2")
        public String test() {
            return "test2";
        }

    }

}