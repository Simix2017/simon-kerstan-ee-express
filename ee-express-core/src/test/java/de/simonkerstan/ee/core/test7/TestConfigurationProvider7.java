/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.test7;

import de.simonkerstan.ee.core.annotations.ConfigurationSource;
import de.simonkerstan.ee.core.configuration.ConfigurationProvider;

import java.util.List;
import java.util.Optional;

/**
 * Configuration provider test implementation for integration tests.
 */
@ConfigurationSource
public class TestConfigurationProvider7 implements ConfigurationProvider {

    @Override
    public Optional<String> getConfigurationValue(String propertyName) {
        if ("my_sub_property.sub_value_1".equals(propertyName)) {
            return Optional.of("VALUE1");
        } else if ("my_sub_property.sub_value_2".equals(propertyName)) {
            return Optional.of("VALUE2");
        }

        return Optional.empty();
    }

    @Override
    public Optional<List<String>> getConfigurationSubValues(String propertyName) {
        if ("my_sub_property".equals(propertyName)) {
            return Optional.of(List.of("sub_value_1", "sub_value_2"));
        }

        return Optional.empty();
    }

}
