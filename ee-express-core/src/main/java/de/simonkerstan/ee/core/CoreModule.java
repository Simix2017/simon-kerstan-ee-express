/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core;

import de.simonkerstan.ee.core.classpath.ClasspathItem;
import de.simonkerstan.ee.core.clazz.ClassHook;
import de.simonkerstan.ee.core.clazz.ConstructorHook;
import de.simonkerstan.ee.core.clazz.MethodHook;
import de.simonkerstan.ee.core.configuration.*;
import de.simonkerstan.ee.core.di.BeanProvider;
import de.simonkerstan.ee.core.exceptions.InvalidConfigurationSourceException;
import de.simonkerstan.ee.core.modules.FrameworkModule;

import java.io.IOException;
import java.util.List;

/**
 * Core framework module.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public class CoreModule implements FrameworkModule {

    private final ConfigurationSourceHook configurationSourceHook = new ConfigurationSourceHook();

    @Override
    public void init(Configuration configuration, ClasspathItem classpathItem) {
        if (configuration instanceof DefaultConfiguration defaultConfiguration) {
            // Dirty hack to get the configuration with an automatically registered provider of command-line arguments.
            // If the configuration has the wrong format, nothing will be done in this module

            // Add system properties (second source)
            final var systemPropertiesConfigurationProvider = new SystemPropertiesConfigurationProvider();
            defaultConfiguration.addConfigurationProvider(systemPropertiesConfigurationProvider);

            // Add custom sources (third source)
            this.configurationSourceHook.getConfigurationSources()
                    .forEach(defaultConfiguration::addConfigurationProvider);

            // Add environment variables (fifth source)
            final var environmentConfigurationProvider = new EnvironmentConfigurationProvider();
            defaultConfiguration.addConfigurationProvider(environmentConfigurationProvider);

            // Add the properties file from the classpath (last source)
            if (classpathItem.isResourceExisting("application.properties")) {
                try (final var is = classpathItem.getResourceAsStream("application.properties")) {
                    final var propertiesFileConfigurationProvider = new PropertiesFileConfigurationProvider(is);
                    defaultConfiguration.addConfigurationProvider(propertiesFileConfigurationProvider);
                } catch (IOException e) {
                    throw new InvalidConfigurationSourceException("Cannot read application.properties from classpath",
                                                                  e);
                }
            }
        }
    }

    @Override
    public List<ClassHook> classHooks() {
        return List.of(this.configurationSourceHook);
    }

    @Override
    public List<ConstructorHook> constructorHooks() {
        return List.of();
    }

    @Override
    public List<MethodHook> methodHooks() {
        return List.of();
    }

    @Override
    public List<BeanProvider<?>> beanProviders() {
        return List.of();
    }

}
