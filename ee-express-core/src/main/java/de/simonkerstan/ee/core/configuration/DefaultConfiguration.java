/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import de.simonkerstan.ee.core.exceptions.MissingConfigurationPropertyException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Default configuration implementation.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public final class DefaultConfiguration implements Configuration {

    /**
     * List of all configuration providers. The first provider in the list will be queried first.
     */
    private final List<ConfigurationProvider> providers = new LinkedList<>();
    /**
     * Resolver for configuration properties (from all providers in the correct order with an attribute converter).
     */
    private final ConfigurationPropertyResolver configurationPropertyResolver = new ConfigurationPropertyResolver(
            propertyName -> this.providers.stream()
                    .map(provider -> provider.getConfigurationValue(propertyName))
                    .filter(Optional::isPresent)
                    .flatMap(Optional::stream)
                    .findFirst());

    /**
     * Create a new configuration instance.
     *
     * @param args Command line arguments
     */
    public DefaultConfiguration(String[] args) {
        this.providers.add(new CommandLineConfigurationProvider(args));
    }

    @Override
    public <T> Optional<T> getPropertyValue(String propertyName, Class<T> type) {
        return this.configurationPropertyResolver.resolveConfigurationValue(propertyName, type);
    }

    @Override
    public <T> T getPropertyValue(String propertyName, Class<T> type, T defaultValue) {
        return this.getPropertyValue(propertyName, type)
                .orElse(defaultValue);
    }

    @Override
    public <T> T getRequiredPropertyValue(String propertyName, Class<T> type) throws
            MissingConfigurationPropertyException {
        return this.getPropertyValue(propertyName, type)
                .orElseThrow(() -> new MissingConfigurationPropertyException(propertyName));
    }

    /**
     * Add a configuration provider.
     *
     * @param provider Configuration provider to be added
     */
    public void addConfigurationProvider(ConfigurationProvider provider) {
        this.providers.add(provider);
    }

}
