package de.simonkerstan.ee.core.modules.test;

import de.simonkerstan.ee.core.clazz.ClassHook;
import de.simonkerstan.ee.core.clazz.ConstructorHook;
import de.simonkerstan.ee.core.clazz.MethodHook;
import de.simonkerstan.ee.core.di.BeanProvider;
import de.simonkerstan.ee.core.modules.FrameworkModule;

import java.util.List;

/**
 * Test framework module for integration and unit tests. This module will never be instantiated.
 */
public class TestModule2 implements FrameworkModule {

    private CoolFrameworkBean coolFrameworkBean;

    @Override
    public void init() {
        // If the framework bean is set, the module was properly initialized.
        this.coolFrameworkBean = new CoolFrameworkBean("Never instantiated bean");
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
        return List.of(new BeanProvider<>(CoolFrameworkBean.class, this.coolFrameworkBean, 0));
    }

}
