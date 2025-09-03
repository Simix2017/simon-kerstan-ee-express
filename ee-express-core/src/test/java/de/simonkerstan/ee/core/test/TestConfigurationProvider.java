/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.test;

import de.simonkerstan.ee.core.annotations.ConfigurationSource;
import de.simonkerstan.ee.core.configuration.ConfigurationProvider;

import java.util.Optional;

/**
 * Configuration provider test implementation for integration tests.
 */
@ConfigurationSource
public class TestConfigurationProvider implements ConfigurationProvider {

    @Override
    public Optional<String> getConfigurationValue(String propertyName) {
        if ("my_property".equals(propertyName)) {
            return Optional.of("Cool ;)");
        }

        return Optional.empty();
    }

}
