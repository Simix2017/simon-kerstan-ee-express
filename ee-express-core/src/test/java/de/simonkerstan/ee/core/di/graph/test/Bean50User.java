/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di.graph.test;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Test bean with a dependency of a runnable (provided by a bean with a priority of {@code 50}).
 */
@RequiredArgsConstructor
@Getter
public class Bean50User {

    private final Runnable bean50;

}
