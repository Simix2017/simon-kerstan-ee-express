/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.persistence.relational;

import de.simonkerstan.ee.core.annotations.MainApplication;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

@MainApplication
public class RelApp implements Runnable {

    private final EntityManagerFactory entityManagerFactory;
    private final TestRepository testRepository;

    @Inject
    public RelApp(EntityManagerFactory entityManagerFactory, TestRepository testRepository) {
        this.entityManagerFactory = entityManagerFactory;
        this.testRepository = testRepository;
    }

    @Override
    public void run() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try (entityManager) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            // Persist a test entity
            entityManager.persist(new TestEntity(null, "It's me", null));
            RelAppStatic.setTestEntity(entityManager.find(TestEntity.class, 1L));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }

        RelAppStatic.setTestEntity2(this.testRepository.findById(1L));
    }

}
