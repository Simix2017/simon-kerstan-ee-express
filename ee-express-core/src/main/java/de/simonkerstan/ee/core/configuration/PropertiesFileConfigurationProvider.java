/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Configuration provider that reads the configuration from the command line.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public final class PropertiesFileConfigurationProvider implements ConfigurationProvider {

    private final Properties properties = new Properties();

    public PropertiesFileConfigurationProvider(InputStream propertiesFile) throws IOException {
        this.properties.load(propertiesFile);
    }

    @Override
    public Optional<String> getConfigurationValue(String propertyName) {
        return Optional.ofNullable(this.properties.getProperty(propertyName));
    }

    @Override
    public Optional<List<String>> getConfigurationSubValues(String propertyName) {
        return Optional.of(this.properties.stringPropertyNames()
                                   .stream()
                                   .filter(name -> name.startsWith(propertyName + "."))
                                   .map(name -> name.substring(propertyName.length() + 1))
                                   .map(name -> name.split("\\.")[0])
                                   .toList());
    }

}
