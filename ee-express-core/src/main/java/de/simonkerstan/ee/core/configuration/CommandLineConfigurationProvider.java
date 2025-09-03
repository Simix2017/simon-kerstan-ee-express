/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Configuration provider that reads the configuration from the command line.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
final class CommandLineConfigurationProvider implements ConfigurationProvider {

    private static final Pattern PROPERTY_PATTERN = Pattern.compile("^--([a-zA-Z0-9._]+)$");
    private static final Pattern PROPERTY_VALUE_PATTERN = Pattern.compile("^--([a-zA-Z0-9._]+)=(.+)$");

    private final Map<String, String> configurationValues = new HashMap<>();

    public CommandLineConfigurationProvider(String[] args) {
        String key = null;
        String value = null;
        for (final var arg : args) {
            if (key == null) {
                // We need to get a key before we can get a value
                final var keyValueMatcher = PROPERTY_VALUE_PATTERN.matcher(arg);
                final var keyMatcher = PROPERTY_PATTERN.matcher(arg);
                if (keyValueMatcher.matches()) {
                    // Key-value pair
                    key = keyValueMatcher.group(1);
                    value = keyValueMatcher.group(2);
                } else if (keyMatcher.matches()) {
                    // Key only
                    key = keyMatcher.group(1);
                } else {
                    // No key
                    continue;
                }
            } else {
                // We already have a key, so we need a value
                value = arg;
            }

            if (value != null) {
                // Key value pair exists
                this.configurationValues.put(key, value);
                key = null;
                value = null;
            }
        }
    }

    @Override
    public Optional<String> getConfigurationValue(String propertyName) {
        return Optional.ofNullable(this.configurationValues.get(propertyName));
    }

}
