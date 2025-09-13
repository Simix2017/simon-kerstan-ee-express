/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.persistence.hibernate;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.StatelessSession;

/**
 * Hibernate initialization result.
 *
 * @param entityManagerFactory Entity manager factory
 * @param statelessSession     Stateless statelessSession
 *                             <p>
 *                             FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public record HibernateInitResult(EntityManagerFactory entityManagerFactory, StatelessSession statelessSession) {
}
