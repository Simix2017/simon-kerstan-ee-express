/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di;

import de.simonkerstan.ee.core.annotations.BeanPriority;
import de.simonkerstan.ee.core.clazz.ClassHook;
import de.simonkerstan.ee.core.clazz.ClassInterfacesHook;
import de.simonkerstan.ee.core.clazz.ConstructorHook;
import de.simonkerstan.ee.core.clazz.MethodHook;
import de.simonkerstan.ee.core.di.graph.DependencyGraph;
import de.simonkerstan.ee.core.di.graph.ObjectBeanCreationInformation;
import de.simonkerstan.ee.core.modules.BeanInstanceProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Hook for dependency injection used by the class scanning mechanism.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
@Slf4j
public class DependencyInjectionHook implements ClassHook, ClassInterfacesHook, ConstructorHook, MethodHook,
        BeanInstanceProvider {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    /**
     * Map of all unprocessed beans. (concrete implementation class -> bean information)
     */
    private final Map<Class<?>, BeanInformation> unprocessedBeans = new HashMap<>();
    /**
     * Map of all beans created by framework modules in the loading mechanism. (type -> bean instance)
     */
    private final Map<Class<?>, Object> frameworkModuleBeans = new HashMap<>();
    private final DependencyGraph dependencyGraph = new DependencyGraph();

    /*
    This hook gets all annotated methods and classes used by Jakarta CDI and creates all beans and contexts after
    scanning in the "postProcess" method.
     */

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends Annotation>[] getClassAnnotations() {
        return new Class[]{Singleton.class, BeanPriority.class};
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends Annotation>[] getConstructorAnnotations() {
        return new Class[]{Inject.class, Singleton.class};
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends Annotation>[] getMethodAnnotations() {
        return new Class[0];
    }

    @Override
    public void processClass(Class<?> clazz, Class<? extends Annotation> annotation, Annotation annotationInstance) {
        log.debug("Processing annotation {} from class {} constructor", annotation.getName(), clazz.getName());
        final var beanInformation = this.unprocessedBeans.computeIfAbsent(clazz, _clazz -> new BeanInformation());
        if (annotation == Singleton.class) {
            // Singleton bean (class annotation)
            beanInformation.setSingleton(true);
        } else if (annotation == BeanPriority.class) {
            // Bean priority (class annotation)
            beanInformation.setPriority(((BeanPriority) annotationInstance).value());
        }
    }

    @Override
    public void processClassInterfaces(Class<?> clazz, Class<?>[] interfaces) {
        final var constructors = Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> constructor.canAccess(null))
                .toList();
        if (constructors.size() == 1 && constructors.get(0)
                .getParameterTypes().length == 0) {
            // Only default constructor
            log.debug("Processing default constructor of class {}", clazz.getName());
            this.dependencyGraph.addDefaultConstructorClass(clazz, interfaces);
        }
    }

    @Override
    public void processConstructor(Constructor<?> constructor, Class<? extends Annotation> annotation,
                                   Annotation annotationInstance) {
        log.debug("Processing constructor annotation {} from class {} constructor", annotation.getName(),
                  constructor.getDeclaringClass()
                          .getName());
        final var beanInformation = this.unprocessedBeans.computeIfAbsent(constructor.getDeclaringClass(),
                                                                          _clazz -> new BeanInformation());
        if (annotation == Inject.class) {
            // Bean constructor for injection
            beanInformation.setBeanType(constructor.getDeclaringClass());
            beanInformation.setConstructor(constructor);
        } else if (annotation == Singleton.class) {
            // Singleton bean (constructor annotation)
            beanInformation.setSingleton(true);
        }
    }

    @Override
    public void processMethod(Method method, Class<? extends Annotation> annotation, Annotation annotationInstance) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> getBeanInstance(Class<T> type) {
        // This method is used in the framework module loading mechanism to get all created beans from other modules.
        return Optional.ofNullable((T) this.frameworkModuleBeans.get(type));
    }

    /**
     * Add a bean provider from a framework module.
     *
     * @param beanProvider Bean provider to be added
     */
    public void addBeanProvider(BeanProvider<?> beanProvider) {
        final var beanCreationInformation = new ObjectBeanCreationInformation(beanProvider.instance());
        this.dependencyGraph.addBean(beanProvider.priority(), beanProvider.type(), beanCreationInformation);
        // Add the bean to the framework module map to be retrieved later in {@link #getBeanInstance(Class)}
        this.frameworkModuleBeans.put(beanProvider.type(), beanProvider.instance());
    }

    /**
     * Get an instantiated bean of the given class.
     *
     * @param clazz Class of the bean
     * @param <T>   Bean type
     * @return Bean instance or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        return (T) this.beans.get(clazz);
    }

    /**
     * Get all instantiated beans.
     *
     * @return Beans
     */
    public Map<Class<?>, Object> getBeans() {
        return Collections.unmodifiableMap(this.beans);
    }

    /**
     * Process all scanned classes and methods to create beans and contexts.
     */
    public void postProcess() {
        this.unprocessedBeans.forEach((clazz, beanInformation) -> {
            this.dependencyGraph.addBean(beanInformation.getPriority(), clazz,
                                         beanInformation.createBeanCreationInformation());
        });

        final var beans = this.dependencyGraph.instantiateBeans();
        this.beans.putAll(beans);
    }

}
