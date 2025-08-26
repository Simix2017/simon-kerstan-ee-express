/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core;

import de.simonkerstan.ee.core.configuration.Configuration;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Application context with the necessary state for the application.
 */
@Data
@RequiredArgsConstructor
public class ApplicationContext {

    private final Configuration configuration;
    private final String[] bootstrapPackages;
    private final Map<Class<?>, Object> beans;
    private final Runnable mainApplication;

}
