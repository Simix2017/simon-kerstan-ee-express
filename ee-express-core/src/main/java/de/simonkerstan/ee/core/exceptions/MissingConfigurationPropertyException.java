/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.exceptions;

import lombok.experimental.StandardException;

/**
 * Exception thrown when a required configuration property is missing.
 */
@StandardException
public class MissingConfigurationPropertyException extends Exception {
}
