/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.persistence.relational;

import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;

/**
 * Test Jakarta Data repository.
 */
@Repository
public interface TestRepository {

    @Find
    TestEntity findById(Long id);

}
