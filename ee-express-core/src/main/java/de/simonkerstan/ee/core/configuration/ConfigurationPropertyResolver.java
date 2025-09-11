/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Resolver for configuration properties (to get the correct type).
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
@RequiredArgsConstructor
@Slf4j
final class ConfigurationPropertyResolver {

    private final Function<String, Optional<String>> configurationValueProvider;

    /**
     * Resolve a configuration property.
     *
     * @param propertyName Name of the property
     * @param propertyType Type of the property
     * @param <T>          Type of the property
     * @return Resolved property value or empty if the property is missing
     */
    public <T> Optional<T> resolveConfigurationValue(String propertyName, Class<T> propertyType) {
        if (propertyType.isRecord()) {
            // Record type
            return this.parseRecord(propertyName, propertyType);
        }

        return this.configurationValueProvider.apply(propertyName)
                .map(value -> this.parsePrimitiveValue(propertyType, value))
                .stream()
                .flatMap(Optional::stream)
                .findAny();
    }

    private <T> Optional<T> parseRecord(String propertyName, Class<T> type) {
        final var recordComponents = type.getRecordComponents();
        final List<Class<?>> constructorTypes = new ArrayList<>(recordComponents.length);
        final List<Object> parameters = new ArrayList<>(recordComponents.length);
        for (final var recordComponent : recordComponents) {
            this.resolveConfigurationValue("%s.%s".formatted(propertyName, recordComponent.getName()),
                                           recordComponent.getType())
                    .ifPresent(e -> {
                        constructorTypes.add(recordComponent.getType());
                        parameters.add(e);
                    });
        }

        if (parameters.size() != recordComponents.length) {
            // Not all parameters could be resolved
            log.warn("Not all record components could be resolved for record type {} ({})", type.getName(),
                     propertyName);
            return Optional.empty();
        }

        try {
            return Optional.of(type.getConstructor(constructorTypes.toArray(Class[]::new))
                                       .newInstance(parameters.toArray()));
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            log.warn("Cannot instantiate record type {} ({})", type.getName(), propertyName, e);
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Optional<T> parsePrimitiveValue(Class<T> propertyType, String value) {
        if (propertyType == String.class) {
            // String
            return (Optional<T>) Optional.of(value);
        } else if (propertyType == char.class || propertyType == Character.class) {
            // Character
            return (Optional<T>) this.parseChar(value);
        } else if (propertyType == boolean.class || propertyType == Boolean.class) {
            // Boolean
            return (Optional<T>) this.parseBool(value);
        } else if (propertyType == byte.class || propertyType == Byte.class) {
            // Byte
            return (Optional<T>) this.parseByte(value);
        } else if (propertyType == short.class || propertyType == Short.class) {
            // Short
            return (Optional<T>) this.parseShort(value);
        } else if (propertyType == int.class || propertyType == Integer.class) {
            // Integer
            return (Optional<T>) this.parseInt(value);
        } else if (propertyType == long.class || propertyType == Long.class) {
            // Long
            return (Optional<T>) this.parseLong(value);
        } else if (propertyType == float.class || propertyType == Float.class) {
            // Float
            return (Optional<T>) this.parseFloat(value);
        } else if (propertyType == double.class || propertyType == Double.class) {
            // Double
            return (Optional<T>) this.parseDouble(value);
        } else if (propertyType.isEnum()) {
            // Enum type
            return this.parseEnum(value, propertyType);
        }

        // Best effort casting
        return Optional.of(propertyType.cast(value));
    }

    private Optional<Character> parseChar(String value) {
        if (value.length() != 1) {
            log.warn("Cannot parse configuration property value as character, value is not a single character");
            return Optional.empty();
        }
        return Optional.of(value.charAt(0));
    }

    private Optional<Boolean> parseBool(String value) {
        return Optional.of(Boolean.parseBoolean(value));
    }

    private Optional<Byte> parseByte(String value) {
        try {
            return Optional.of(Byte.parseByte(value));
        } catch (NumberFormatException e) {
            log.warn("Cannot parse configuration property value as byte", e);
            return Optional.empty();
        }
    }

    private Optional<Short> parseShort(String value) {
        try {
            return Optional.of(Short.parseShort(value));
        } catch (NumberFormatException e) {
            log.warn("Cannot parse configuration property value as short", e);
            return Optional.empty();
        }
    }

    private Optional<Integer> parseInt(String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            log.warn("Cannot parse configuration property value as integer", e);
            return Optional.empty();
        }
    }

    private Optional<Long> parseLong(String value) {
        try {
            return Optional.of(Long.parseLong(value));
        } catch (NumberFormatException e) {
            log.warn("Cannot parse configuration property value as long", e);
            return Optional.empty();
        }
    }

    private Optional<Float> parseFloat(String value) {
        try {
            return Optional.of(Float.parseFloat(value));
        } catch (NumberFormatException e) {
            log.warn("Cannot parse configuration property value as float", e);
            return Optional.empty();
        }
    }

    private Optional<Double> parseDouble(String value) {
        try {
            return Optional.of(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            log.warn("Cannot parse configuration property value as double", e);
            return Optional.empty();
        }
    }

    private <T> Optional<T> parseEnum(String propertyName, Class<T> propertyType) {
        return Arrays.stream(propertyType.getEnumConstants())
                .filter(e -> ((Enum<?>) e).name()
                        .equals(propertyName))
                .findAny();
    }

}
