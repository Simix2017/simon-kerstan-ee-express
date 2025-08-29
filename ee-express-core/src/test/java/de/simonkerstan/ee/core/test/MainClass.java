/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.test;

import de.simonkerstan.ee.core.annotations.MainApplication;
import de.simonkerstan.ee.core.modules.test.CoolFrameworkBean;
import jakarta.inject.Inject;

/**
 * Test main class setting a property which can be used in the integration tests.
 */
@MainApplication
public class MainClass implements Runnable {

    private final TestService testService;
    private final CoolFrameworkBean coolFrameworkBean;

    @Inject
    public MainClass(TestService testService, CoolFrameworkBean coolFrameworkBean) {
        this.testService = testService;
        this.coolFrameworkBean = coolFrameworkBean;
    }

    @Override
    public void run() {
        TestStaticHolder.setTestProperty(this.testService.helloWorld());
        TestStaticHolder.setTestBeanProviderProperty(this.coolFrameworkBean.test());
    }

}
