/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.test7;

import de.simonkerstan.ee.core.annotations.MainApplication;
import de.simonkerstan.ee.core.configuration.Configuration;
import de.simonkerstan.ee.core.exceptions.MissingConfigurationPropertyException;
import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test application for testing the application bootstrapper (configuration handling).
 */
@MainApplication
public class TestApp7 implements Runnable {

    private final Configuration configuration;

    @Inject
    public TestApp7(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void run() {
        try {
            Test7.setTestList(this.configuration.getRequiredListValue("my_sub_property", String.class));
            Test7.setTestMap(this.configuration.getRequiredMapValue("my_sub_property", String.class));

            Test7.setXyzFromPropertiesFile(this.configuration.getRequiredPropertyValue("xyz", String.class));
        } catch (MissingConfigurationPropertyException e) {
            fail("Cannot get required configuration property", e);
        }
    }

}
