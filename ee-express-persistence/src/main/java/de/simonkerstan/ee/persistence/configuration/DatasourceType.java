/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.persistence.configuration;

/**
 * Datasource type.
 */
public enum DatasourceType {

    /**
     * Relational database datasource.
     */
    RELATIONAL,

    /**
     * Redis distributed cache datasource.
     */
    REDIS

}
