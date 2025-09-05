/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.test;

import lombok.Getter;
import lombok.Setter;

/**
 * Property holder for an integration test.
 */
public class TestStaticHolder {

    @Setter
    @Getter
    private static String testProperty = "";

    @Getter
    @Setter
    private static String testBeanProviderProperty = "";

    @Getter
    @Setter
    private static int testSystemPropertiesConfigurationProperty;

    @Getter
    @Setter
    private static boolean testCommandlineConfigurationProperty;

    @Getter
    @Setter
    private static String testCustomSourceConfigurationProperty;

    @Getter
    @Setter
    private static String testEnvironmentConfigurationProperty;

    @Getter
    @Setter
    private static int testApplicationPropertiesClasspathConfigurationProperty;

}
