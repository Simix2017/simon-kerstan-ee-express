/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.exceptions;

import lombok.experimental.StandardException;

/**
 * Exception thrown when the configuration source is invalid.
 */
@StandardException
public class InvalidConfigurationSourceException extends RuntimeException {
}
