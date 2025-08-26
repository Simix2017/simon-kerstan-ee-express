/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import lombok.experimental.StandardException;

/**
 * Exception thrown when a required configuration property is missing.
 */
@StandardException
public class MissingPropertyException extends Exception {
}
