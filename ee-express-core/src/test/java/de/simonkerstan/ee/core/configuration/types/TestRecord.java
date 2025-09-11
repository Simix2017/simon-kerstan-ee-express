/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration.types;

/**
 * Test record for testing the configuration property resolver.
 *
 * @param helloWorld The hello world string
 * @param port       The port number
 */
public record TestRecord(String helloWorld, int port) {
}
