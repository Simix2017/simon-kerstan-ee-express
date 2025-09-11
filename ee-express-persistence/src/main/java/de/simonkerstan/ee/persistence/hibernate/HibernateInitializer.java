/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.persistence.hibernate;

import de.simonkerstan.ee.persistence.configuration.JpaDatasourceConfiguration;
import jakarta.persistence.EntityManagerFactory;
import jakarta.validation.ValidatorFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Initializer for Hibernate (used for JPA).
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public final class HibernateInitializer {

    private HibernateInitializer() {
    }

    public static EntityManagerFactory init(JpaDatasourceConfiguration datasource, ValidatorFactory validatorFactory) {
        // Configure the service registry (from Hibernate)
        final var standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();
        configureDatasource(standardServiceRegistryBuilder, datasource);
        standardServiceRegistryBuilder.applySetting("jakarta.persistence.validation.factory", validatorFactory);
        final var servicesRegistry = standardServiceRegistryBuilder
                .build();

        // Create the session factory
        final var sessionFactory = new MetadataSources(servicesRegistry).buildMetadata()
                .buildSessionFactory();
        return sessionFactory;
    }

    private static void configureDatasource(StandardServiceRegistryBuilder standardServiceRegistryBuilder,
                                            JpaDatasourceConfiguration datasource) {
        datasource.toJpaProperties()
                .forEach(standardServiceRegistryBuilder::applySetting);
    }

}
