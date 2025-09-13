/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import java.util.List;
import java.util.Optional;

/**
 * Provider for configuration property values.
 */
public interface ConfigurationProvider {

    /**
     * Get the value of a configuration property.
     *
     * @param propertyName Name of the property
     * @return Value of the property or empty if the property is missing
     */
    Optional<String> getConfigurationValue(String propertyName);

    /**
     * Get the sub values of a configuration property (e.g., map keys or list elements).
     *
     * @param propertyName Name of the property
     * @return Sub values of the property or empty if the property is missing or lists and maps are not supported
     */
    Optional<List<String>> getConfigurationSubValues(String propertyName);

}
