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
final class CommandLineConfigurationProvider implements ConfigurationProvider {

    public CommandLineConfigurationProvider(String[] args) {
    }

    @Override
    public Optional<String> getConfigurationValue(String propertyName) {
        return Optional.empty();
    }

}
