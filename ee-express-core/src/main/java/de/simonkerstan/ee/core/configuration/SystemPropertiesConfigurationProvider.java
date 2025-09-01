/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import java.util.Optional;

/**
 * Configuration properties from the system properties (automatically loaded by the JVM).
 */
public class SystemPropertiesConfigurationProvider implements ConfigurationProvider {

    @Override
    public Optional<String> getConfigurationValue(String propertyName) {
        return Optional.ofNullable(System.getProperty(propertyName));
    }

}
