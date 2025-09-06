/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di;

import de.simonkerstan.ee.core.annotations.BeanPriority;
import de.simonkerstan.ee.core.di.graph.BeanCreationInformation;
import de.simonkerstan.ee.core.di.graph.ConstructorBeanCreationInformation;
import de.simonkerstan.ee.core.di.graph.ObjectBeanCreationInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;

/**
 * Information about a bean to be used to create a new instance.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
class BeanInformation {

    private Class<?> beanType;
    private int priority = BeanPriority.DEFAULT_PRIORITY;
    private boolean singleton = false;
    private Constructor<?> constructor;
    private Object beanInstance;

    /**
     * Create a new instance of {@link BeanCreationInformation} from this bean information.
     *
     * @return Bean creation information
     */
    public BeanCreationInformation createBeanCreationInformation() {
        if (this.constructor != null) {
            // Constructor bean creation
            return new ConstructorBeanCreationInformation(this.constructor, this.singleton);
        }

        // We use the bean instance (which could be null)
        if (this.beanInstance == null) {
            log.warn("Using null bean instance for bean {}.", this.beanType.getName());
        }
        return new ObjectBeanCreationInformation(this.beanInstance);
    }

}
