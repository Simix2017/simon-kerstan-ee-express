/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import de.simonkerstan.ee.core.exceptions.MissingConfigurationPropertyException;

import java.util.List;
import java.util.Map;
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

    /**
     * Get a list value of a configuration property.
     *
     * @param propertyName Name of the property
     * @param type         Type of the list elements
     * @param <T>          Type of the list elements
     * @return List of elements or empty if the property is missing or the property is not a list
     */
    <T> Optional<List<T>> getListValue(String propertyName, Class<T> type);

    /**
     * Get a required list value of a configuration property.
     *
     * @param propertyName Name of the property
     * @param type         Type of the list elements
     * @param <T>          Type of the list elements
     * @return List of elements
     * @throws MissingConfigurationPropertyException If the property is missing or the property is not a list
     */
    <T> List<T> getRequiredListValue(String propertyName, Class<T> type) throws MissingConfigurationPropertyException;

    /**
     * Get a map value of a configuration property.
     *
     * @param propertyName Name of the property
     * @param type         Type of the map values
     * @param <T>          Type of the map values
     * @return Map of elements or empty if the property is missing or the property is not a map
     */
    <T> Optional<Map<String, T>> getMapValue(String propertyName, Class<T> type);

    /**
     * Get a required map value of a configuration property.
     *
     * @param propertyName Name of the property
     * @param type         Type of the map values
     * @param <T>          Type of the map values
     * @return Map of elements
     * @throws MissingConfigurationPropertyException If the property is missing or the property is not a map
     */
    <T> Map<String, T> getRequiredMapValue(String propertyName, Class<T> type) throws
            MissingConfigurationPropertyException;

}
