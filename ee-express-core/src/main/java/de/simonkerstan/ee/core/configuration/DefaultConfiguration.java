/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import java.util.Optional;

/**
 * Default configuration implementation.
 */
public class DefaultConfiguration implements Configuration {

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getPropertyValue(String propertyName, Class<T> type) {
        if ("server.port".equals(propertyName)) {
            // Server port
            return (Optional<T>) Optional.of(8080);
        }

        return Optional.empty();
    }

    @Override
    public <T> T getPropertyValue(String propertyName, T defaultValue) {
        return defaultValue;
    }

    @Override
    public <T> T getRequiredPropertyValue(String propertyName, Class<T> type) throws MissingPropertyException {
        return this.getPropertyValue(propertyName, type)
                .orElseThrow(() -> new MissingPropertyException(propertyName));
    }

}
