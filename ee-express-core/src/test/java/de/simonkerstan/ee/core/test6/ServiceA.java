/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.test6;

import jakarta.inject.Inject;

public class ServiceA {

    private final ServiceB serviceB;

    @Inject
    public ServiceA(ServiceB serviceB) {
        this.serviceB = serviceB;
    }

}
