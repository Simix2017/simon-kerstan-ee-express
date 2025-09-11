/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.persistence;

import de.simonkerstan.ee.core.EeExpressApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for relational persistence.
 */
class RelationalIntegrationTest {

    @Test
    @DisplayName("Given valid relational persistence -> Should persist data")
    void testRelationalPersistence() {
        // Configure the datasource (H2 in-memory database)
        System.setProperty("persistence.source.default.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        System.setProperty("persistence.source.default.username", "sa");
        System.setProperty("persistence.source.default.password", "");
        System.setProperty("persistence.source.default.driverName", "org.h2.Driver");
        System.setProperty("persistence.source.default.type", "RELATIONAL");
        System.setProperty("persistence.source.default.schemaStrategy", "CREATE");

        // Bootstrap the application
        final var applicationConfiguration = EeExpressApplication.initialize(new String[]{},
                                                                             "de.simonkerstan.ee.persistence" +
                                                                                     ".relational");
        EeExpressApplication.run(applicationConfiguration);
    }

}
