/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di;

/**
 * Provider for externally created beans for the CDI context.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public record BeanProvider<T>(Class<T> type, T instance, int priority) {
}
