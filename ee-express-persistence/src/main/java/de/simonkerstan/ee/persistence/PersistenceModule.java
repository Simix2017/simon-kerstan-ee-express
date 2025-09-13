/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.persistence;

import de.simonkerstan.ee.core.classpath.ClasspathItem;
import de.simonkerstan.ee.core.clazz.ClassHook;
import de.simonkerstan.ee.core.clazz.ConstructorHook;
import de.simonkerstan.ee.core.clazz.MethodHook;
import de.simonkerstan.ee.core.configuration.Configuration;
import de.simonkerstan.ee.core.di.BeanProvider;
import de.simonkerstan.ee.core.exceptions.MissingConfigurationPropertyException;
import de.simonkerstan.ee.core.modules.BeanInstanceProvider;
import de.simonkerstan.ee.core.modules.FrameworkModule;
import de.simonkerstan.ee.persistence.configuration.DatasourceType;
import de.simonkerstan.ee.persistence.configuration.JpaDatasourceConfiguration;
import de.simonkerstan.ee.persistence.hibernate.HibernateInitializer;
import jakarta.persistence.EntityManagerFactory;
import jakarta.validation.ValidatorFactory;
import org.hibernate.StatelessSession;

import java.util.LinkedList;
import java.util.List;

/**
 * Persistence framework module.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public class PersistenceModule implements FrameworkModule {

    private final EntityClassHook entityClassHook = new EntityClassHook();
    private EntityManagerFactory entityManagerFactory;
    private StatelessSession statelessSession;

    @Override
    public void init(Configuration configuration, ClasspathItem classpathItem,
                     BeanInstanceProvider beanInstanceProvider) {
        final var validatorFactory = beanInstanceProvider.getBeanInstance(ValidatorFactory.class)
                .orElseThrow();
        try {
            // Get the default datasource configuration
            final var defaultDatasourceType = configuration.getRequiredPropertyValue("persistence.source.default.type",
                                                                                     DatasourceType.class);
            if (defaultDatasourceType == DatasourceType.RELATIONAL) {
                // Initialize JPA for default datasource
                final var defaultDatasource = configuration.getRequiredPropertyValue("persistence.source.default",
                                                                                     JpaDatasourceConfiguration.class);
                final var initResult = HibernateInitializer.init(defaultDatasource, validatorFactory,
                                                                 this.entityClassHook.getEntities());
                this.entityManagerFactory = initResult.entityManagerFactory();
                this.statelessSession = initResult.statelessSession();
            }
        } catch (MissingConfigurationPropertyException e) {
            throw new IllegalArgumentException("Cannot initialize persistence module without default datasource", e);
        }
    }

    @Override
    public List<ClassHook> classHooks() {
        return List.of(this.entityClassHook);
    }

    @Override
    public List<ConstructorHook> constructorHooks() {
        return List.of();
    }

    @Override
    public List<MethodHook> methodHooks() {
        return List.of();
    }

    @Override
    public List<BeanProvider<?>> beanProviders() {
        final List<BeanProvider<?>> beanProviders = new LinkedList<>();
        if (this.entityManagerFactory != null) {
            // Add entity manager factory as bean provider
            beanProviders.add(
                    new BeanProvider<>(EntityManagerFactory.class, this.entityManagerFactory, Integer.MAX_VALUE));
        }
        if (this.statelessSession != null) {
            // Add stateless session as bean provider
            beanProviders.add(new BeanProvider<>(StatelessSession.class, this.statelessSession, Integer.MAX_VALUE));
        }

        return beanProviders;
    }

}
