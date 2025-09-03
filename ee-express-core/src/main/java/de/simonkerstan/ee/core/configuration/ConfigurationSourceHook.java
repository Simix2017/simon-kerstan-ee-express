/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import de.simonkerstan.ee.core.annotations.ConfigurationSource;
import de.simonkerstan.ee.core.clazz.ClassHook;
import de.simonkerstan.ee.core.exceptions.InvalidConfigurationSourceException;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Hook to process custom configuration sources.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public class ConfigurationSourceHook implements ClassHook {

    @Getter
    private final List<ConfigurationProvider> configurationSources = new LinkedList<>();

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends Annotation>[] getClassAnnotations() {
        return new Class[]{ConfigurationSource.class};
    }

    @Override
    public void processClass(Class<?> clazz, Class<? extends Annotation> annotation, Annotation annotationInstance) {
        // We only process one annotation, so we can ignore the annotation parameter
        if (Arrays.stream(clazz.getInterfaces())
                .noneMatch(clazzInterface -> clazzInterface == ConfigurationProvider.class)) {
            // Custom configuration source must implement the ConfigurationProvider interface
            throw new InvalidConfigurationSourceException(
                    "Custom configuration source must implement the ConfigurationProvider interface");
        }

        try {
            this.configurationSources.add((ConfigurationProvider) clazz.getDeclaredConstructor()
                    .newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new InvalidConfigurationSourceException("Cannot instantiate custom configuration source", e);
        }
    }

}
