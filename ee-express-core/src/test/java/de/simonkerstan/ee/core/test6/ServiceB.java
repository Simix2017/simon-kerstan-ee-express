/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.test6;

import jakarta.inject.Inject;

public class ServiceB {

    private final ServiceA serviceA;

    @Inject
    public ServiceB(ServiceA serviceA) {
        this.serviceA = serviceA;
    }

}
