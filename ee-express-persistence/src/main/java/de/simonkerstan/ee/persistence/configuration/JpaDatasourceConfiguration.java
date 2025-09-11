/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.persistence.configuration;

import jakarta.persistence.PersistenceConfiguration;

import java.util.Map;

/**
 * Datasource configuration for Jakarta Persistence API (JPA).
 *
 * @param url            JDBC URL
 * @param username       Username for authentication
 * @param password       Password for authentication
 * @param driverName     Name of the JDBC driver class
 * @param type           Datasource type
 * @param schemaStrategy Strategy for handling the database schema
 */
public record JpaDatasourceConfiguration(String url, String username, String password, String driverName,
                                         DatasourceType type, SchemaStrategy schemaStrategy) {

    /**
     * Get JPA properties for this datasource.
     *
     * @return JPA properties
     */
    public Map<String, String> toJpaProperties() {
        final Map.Entry<String, String> schemaGenerationAction;
        if (schemaStrategy == SchemaStrategy.CREATE) {
            schemaGenerationAction = Map.entry(PersistenceConfiguration.SCHEMAGEN_DATABASE_ACTION, "create");
        } else {
            schemaGenerationAction = Map.entry(PersistenceConfiguration.SCHEMAGEN_DATABASE_ACTION, "none");
        }

        return Map.ofEntries(schemaGenerationAction, //
                             Map.entry(PersistenceConfiguration.JDBC_URL, url), //
                             Map.entry(PersistenceConfiguration.JDBC_USER, username), //
                             Map.entry(PersistenceConfiguration.JDBC_PASSWORD, password), //
                             Map.entry(PersistenceConfiguration.JDBC_DRIVER, driverName) //
        );
    }

}
