/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core;

import de.simonkerstan.ee.core.clazz.ClassHook;
import de.simonkerstan.ee.core.clazz.ConstructorHook;
import de.simonkerstan.ee.core.clazz.MethodHook;
import de.simonkerstan.ee.core.configuration.Configuration;
import de.simonkerstan.ee.core.configuration.DefaultConfiguration;
import de.simonkerstan.ee.core.configuration.PropertiesFileConfigurationProvider;
import de.simonkerstan.ee.core.di.BeanProvider;
import de.simonkerstan.ee.core.modules.FrameworkModule;

import java.util.List;

/**
 * Core framework module.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public class CoreModule implements FrameworkModule {

    @Override
    public void init(Configuration configuration) {
        if (configuration instanceof DefaultConfiguration defaultConfiguration) {
            // Dirty hack to get the configuration with an automatically registered provider of command-line arguments.
            // If the configuration has the wrong format, nothing will be done in this module
            final var propertiesFileConfigurationProvider = new PropertiesFileConfigurationProvider();
            defaultConfiguration.addConfigurationProvider(propertiesFileConfigurationProvider);
        }
    }

    @Override
    public List<ClassHook> classHooks() {
        return List.of();
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
