/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for bean priority to allow overriding bean types.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BeanPriority {

    /**
     * Default priority for beans.
     */
    int DEFAULT_PRIORITY = 100_000;

    /**
     * Priority of the bean. The lower the value, the higher the priority.
     *
     * @return Priority of the bean
     */
    int value();

}
