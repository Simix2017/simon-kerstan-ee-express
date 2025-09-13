/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Loader for configuration file provider.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
@Slf4j
public final class ConfigurationFileLoader {

    private ConfigurationFileLoader() {
    }

    /**
     * Load a configuration file provider.
     *
     * @param providerClass Class of the provider to be loaded
     * @param filename      Filename of the configuration file to be loaded
     * @return Configuration provider or empty if the provider could not be loaded
     */
    public static Optional<ConfigurationProvider> loadProvider(Class<? extends ConfigurationProvider> providerClass,
                                                               String filename) {
        final var path = Path.of(filename);
        try (final var is = Files.newInputStream(path)) {
            if (providerClass == PropertiesFileConfigurationProvider.class) {
                // Properties file provider
                return Optional.of(new PropertiesFileConfigurationProvider(is));
            }
        } catch (IOException e) {
            log.warn("Cannot read configuration file {}", filename, e);
            return Optional.empty();
        }

        log.warn("Cannot recognize configuration provider {}", providerClass);
        return Optional.empty();
    }

}
