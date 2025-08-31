/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Function;

/**
 * Resolver for configuration properties (to get the correct type).
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
@RequiredArgsConstructor
@Slf4j
final class ConfigurationPropertyResolver {

    private final Function<String, Optional<String>> configurationValueProvider;

    /**
     * Resolve a configuration property.
     *
     * @param propertyName Name of the property
     * @param propertyType Type of the property
     * @param <T>          Type of the property
     * @return Resolved property value or empty if the property is missing
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> resolveConfigurationValue(String propertyName, Class<T> propertyType) {
        return (Optional<T>) this.configurationValueProvider.apply(propertyName)
                .map(value -> {
                    if (propertyType == int.class || propertyType == Integer.class) {
                        // Integer
                        return parseInt(value);
                    }

                    // Best effort casting
                    return Optional.of(propertyType.cast(value));
                })
                .stream()
                .flatMap(Optional::stream)
                .findAny();
    }

    private static Optional<Integer> parseInt(String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            log.warn("Cannot parse configuration property value as integer", e);
            return Optional.empty();
        }
    }

}
