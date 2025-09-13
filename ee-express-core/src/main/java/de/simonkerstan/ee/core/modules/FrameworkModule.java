/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.modules;

import de.simonkerstan.ee.core.classpath.ClasspathItem;
import de.simonkerstan.ee.core.clazz.ClassHook;
import de.simonkerstan.ee.core.clazz.ConstructorHook;
import de.simonkerstan.ee.core.clazz.MethodHook;
import de.simonkerstan.ee.core.configuration.Configuration;
import de.simonkerstan.ee.core.di.BeanProvider;

import java.util.List;

/**
 * One module of the framework.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public interface FrameworkModule {

    /**
     * Initialize the module. This method will be called after class scanning, so the results of the hooks can be used.
     *
     * @param configuration        Configuration of the framework
     * @param classpathItem        Classpath item (wrapper to access all classpath resources)
     * @param beanInstanceProvider Provider for already instantiated beans by framework modules loaded before
     */
    void init(Configuration configuration, ClasspathItem classpathItem, BeanInstanceProvider beanInstanceProvider);

    /**
     * Get all class hooks of this module.
     *
     * @return Class hooks
     */
    List<ClassHook> classHooks();

    /**
     * Get all constructor hooks of this module.
     *
     * @return Constructor hooks
     */
    List<ConstructorHook> constructorHooks();

    /**
     * Get all method hooks of this module.
     *
     * @return Method hooks
     */
    List<MethodHook> methodHooks();

    /**
     * Get all bean providers of this module. This method will be called after the module is initialized and all
     * classes are scanned.
     *
     * @return Bean providers
     */
    List<BeanProvider<?>> beanProviders();

}
