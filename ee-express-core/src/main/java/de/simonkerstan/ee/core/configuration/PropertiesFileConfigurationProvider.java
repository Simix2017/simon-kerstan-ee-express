/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import java.util.Optional;

/**
 * Configuration provider that reads the configuration from the command line.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public final class PropertiesFileConfigurationProvider implements ConfigurationProvider {

    @Override
    public Optional<String> getConfigurationValue(String propertyName) {
        // TODO
        if (propertyName.equals("server.port")) {
            return Optional.of("8080");
        }

        return Optional.empty();
    }

}
