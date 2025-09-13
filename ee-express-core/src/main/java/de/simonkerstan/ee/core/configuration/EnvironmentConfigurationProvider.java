/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import java.util.List;
import java.util.Optional;

/**
 * Configuration properties from environment variables (automatically loaded by the JVM).
 */
public final class EnvironmentConfigurationProvider implements ConfigurationProvider {

    /*
    There is no unit test because Mockito cannot mock System.getenv().
     */

    @Override
    public Optional<String> getConfigurationValue(String propertyName) {
        return Optional.ofNullable(System.getenv(propertyName.toUpperCase()
                                                         .replace('.', '_')));
    }

    @Override
    public Optional<List<String>> getConfigurationSubValues(String propertyName) {
        return Optional.empty();
    }

}
