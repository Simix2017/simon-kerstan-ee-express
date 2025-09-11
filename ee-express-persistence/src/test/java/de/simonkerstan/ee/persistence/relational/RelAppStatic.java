/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.persistence.relational;

import lombok.Getter;
import lombok.Setter;

public final class RelAppStatic {

    @Getter
    @Setter
    private static TestEntity testEntity;

    private RelAppStatic() {
    }

}
