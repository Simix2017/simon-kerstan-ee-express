/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.test3;

import de.simonkerstan.ee.core.annotations.MainApplication;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Class with unsatisfied dependencies.
 */
@MainApplication
public class UnsatisfiedApplication implements Runnable {

    private final List<?> list;

    @Inject
    public UnsatisfiedApplication(List<?> list) {
        this.list = list;
    }

    @Override
    public void run() {
        this.list.forEach(System.out::println);
    }

}
