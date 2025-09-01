/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.test;

import de.simonkerstan.ee.core.annotations.MainApplication;
import de.simonkerstan.ee.core.configuration.Configuration;
import de.simonkerstan.ee.core.exceptions.MissingConfigurationPropertyException;
import de.simonkerstan.ee.core.modules.test.CoolFrameworkBean;
import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test main class setting a property which can be used in the integration tests.
 */
@MainApplication
public class MainClass implements Runnable {

    private final Configuration configuration;
    private final TestService testService;
    private final CoolFrameworkBean coolFrameworkBean;

    @Inject
    public MainClass(Configuration configuration, TestService testService, CoolFrameworkBean coolFrameworkBean) {
        this.configuration = configuration;
        this.testService = testService;
        this.coolFrameworkBean = coolFrameworkBean;
    }

    @Override
    public void run() {
        try {
            TestStaticHolder.setTestConfigurationProperty(
                    this.configuration.getRequiredPropertyValue("server.port", Integer.class));
            TestStaticHolder.setTestConfigurationProperty2(
                    this.configuration.getRequiredPropertyValue("server.alternative_port", Integer.class));
        } catch (MissingConfigurationPropertyException e) {
            fail("Cannot read required configuration property", e);
        }
        TestStaticHolder.setTestProperty(this.testService.helloWorld());
        TestStaticHolder.setTestBeanProviderProperty(this.coolFrameworkBean.test());
    }

}
