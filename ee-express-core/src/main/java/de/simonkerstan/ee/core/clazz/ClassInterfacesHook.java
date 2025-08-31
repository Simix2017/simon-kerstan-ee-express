/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.clazz;

/**
 * Hook to process all directly implemented interfaces of a class.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public interface ClassInterfacesHook {

    /**
     * Process all interfaces directly implemented by the given class. This method will only be called for real classes,
     * not for interfaces or other types.
     *
     * @param clazz      Class to process
     * @param interfaces Directly implemented interfaces of the given class
     */
    void processClassInterfaces(Class<?> clazz, Class<?>[] interfaces);

}
