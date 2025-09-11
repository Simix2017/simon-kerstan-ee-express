/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.persistence.relational;

import de.simonkerstan.ee.core.annotations.MainApplication;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;

@MainApplication
public class RelApp implements Runnable {

    private final EntityManagerFactory entityManagerFactory;

    @Inject
    public RelApp(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void run() {

    }

}
