/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.modules;

import java.util.Optional;

/**
 * Provider for externally created beans (which were created by framework modules loaded before).
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public interface BeanInstanceProvider {

    /**
     * Get an instance of a bean.
     *
     * @param type Type of the bean to get
     * @param <T>  Bean type
     * @return Bean instance or empty if no bean of the given type is available
     */
    <T> Optional<T> getBeanInstance(Class<T> type);

}
