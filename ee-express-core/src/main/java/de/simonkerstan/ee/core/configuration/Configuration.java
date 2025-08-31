/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import de.simonkerstan.ee.core.exceptions.MissingConfigurationPropertyException;

import java.util.Optional;

/**
 * Application configuration.
 */
public interface Configuration {

    /**
     * Get the value of a configuration property.
     *
     * @param propertyName Name of the property
     * @param type         Type of the property value
     * @param <T>          Type of the property value
     * @return Value of the property or empty if the property is missing
     */
    <T> Optional<T> getPropertyValue(String propertyName, Class<T> type);

    /**
     * Get the value of a configuration property.
     *
     * @param propertyName Name of the property
     * @param type         Type of the property value
     * @param defaultValue Default value if the property is missing
     * @param <T>          Type of the property value
     * @return Value of the property or the default value if the property is missing
     */
    <T> T getPropertyValue(String propertyName, Class<T> type, T defaultValue);

    /**
     * Get a required value of a configuration property.
     *
     * @param propertyName Name of the property
     * @param type         Type of the property value
     * @param <T>          Type of the property value
     * @return Value of the property
     * @throws MissingConfigurationPropertyException If the property is missing
     */
    <T> T getRequiredPropertyValue(String propertyName, Class<T> type) throws MissingConfigurationPropertyException;

}
