/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.persistence.hibernate;

import de.simonkerstan.ee.persistence.configuration.JpaDatasourceConfiguration;
import jakarta.validation.ValidatorFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

/**
 * Initializer for Hibernate (used for JPA).
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public final class HibernateInitializer {

    private HibernateInitializer() {
    }

    /**
     * Initialize Hibernate for a datasource.
     *
     * @param datasource       Datasource configuration
     * @param validatorFactory Validator factory to be used for validation
     * @param entities         Entities to be registered in Hibernate
     * @return Initialization result
     */
    public static HibernateInitResult init(JpaDatasourceConfiguration datasource, ValidatorFactory validatorFactory,
                                           List<Class<?>> entities) {
        // Configure the service registry (from Hibernate)
        final var standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();
        configureDatasource(standardServiceRegistryBuilder, datasource);
        standardServiceRegistryBuilder.applySetting("jakarta.persistence.validation.factory", validatorFactory);
        final var servicesRegistry = standardServiceRegistryBuilder.build();

        // Create the session factory
        final var metadataSources = new MetadataSources(servicesRegistry);
        entities.forEach(metadataSources::addAnnotatedClass);
        final var sessionFactory = metadataSources.buildMetadata()
                .buildSessionFactory();
        // Create a stateless statelessSession
        final var statelessSession = sessionFactory.openStatelessSession();
        return new HibernateInitResult(sessionFactory, statelessSession);
    }

    private static void configureDatasource(StandardServiceRegistryBuilder standardServiceRegistryBuilder,
                                            JpaDatasourceConfiguration datasource) {
        datasource.toJpaProperties()
                .forEach(standardServiceRegistryBuilder::applySetting);
    }

}
