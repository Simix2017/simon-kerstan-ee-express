/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.modules.test;

import de.simonkerstan.ee.core.classpath.ClasspathItem;
import de.simonkerstan.ee.core.clazz.ClassHook;
import de.simonkerstan.ee.core.clazz.ConstructorHook;
import de.simonkerstan.ee.core.clazz.MethodHook;
import de.simonkerstan.ee.core.configuration.Configuration;
import de.simonkerstan.ee.core.di.BeanProvider;
import de.simonkerstan.ee.core.modules.BeanInstanceProvider;
import de.simonkerstan.ee.core.modules.FrameworkModule;
import lombok.Getter;

import java.util.List;

/**
 * Framework module loaded after the others.
 */
public class AfterModule implements FrameworkModule {

    @Getter
    private CoolFrameworkBean coolFrameworkBean;

    @Override
    public void init(Configuration configuration, ClasspathItem classpathItem,
                     BeanInstanceProvider beanInstanceProvider) {
        // Get bean from a framework module loaded before and throw an exception if not available
        this.coolFrameworkBean = beanInstanceProvider.getBeanInstance(CoolFrameworkBean.class)
                .orElseThrow(() -> new IllegalStateException("Bean not available"));
    }

    @Override
    public List<ClassHook> classHooks() {
        return List.of();
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
        return List.of();
    }

}
