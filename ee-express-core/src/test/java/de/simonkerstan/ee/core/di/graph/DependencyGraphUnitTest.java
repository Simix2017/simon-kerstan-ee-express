/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di.graph;

import de.simonkerstan.ee.core.di.graph.test.SingletonA;
import de.simonkerstan.ee.core.di.graph.test.SingletonB;
import de.simonkerstan.ee.core.di.graph.test.TestSingletons1;
import de.simonkerstan.ee.core.di.graph.test.TestSingletons2;
import de.simonkerstan.ee.core.test.MainClass;
import de.simonkerstan.ee.core.test.TestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DependencyGraphUnitTest {

    @Test
    @DisplayName("Create a new graph and insert a class with unresolved dependencies at first and then the " +
            "dependency -> Should result in a satisfied graph")
    void testAdd() throws NoSuchMethodException {
        final var tested = new DependencyGraph();

        // Add a class with unresolved dependencies
        tested.addBean(0, MainClass.class,
                       new ConstructorBeanCreationInformation(MainClass.class.getConstructor(TestService.class), true),
                       new Class<?>[]{TestService.class});
        assertTrue(tested.hasUnresolvedDependencies());

        // Add the dependency
        tested.addBean(0, TestService.class,
                       new ConstructorBeanCreationInformation(TestService.class.getConstructor(), true),
                       new Class<?>[]{});
        assertFalse(tested.hasUnresolvedDependencies());
    }

    @Test
    @DisplayName("Instantiate the beans -> Should instantiate all beans in the graph")
    void testInstantiateBeans() throws NoSuchMethodException {
        final var tested = new DependencyGraph();

        // Add beans
        tested.addBean(0, MainClass.class,
                       new ConstructorBeanCreationInformation(MainClass.class.getConstructor(TestService.class), true),
                       new Class<?>[]{TestService.class});
        tested.addBean(0, TestService.class,
                       new ConstructorBeanCreationInformation(TestService.class.getConstructor(), true),
                       new Class<?>[]{});

        // Instantiate the beans
        final var result = tested.instantiateBeans();
        assertEquals(3, result.size());
        assertTrue(result.containsKey(MainClass.class));
        assertTrue(result.containsKey(Runnable.class));
        assertTrue(result.containsKey(TestService.class));
        assertNotNull(result.get(MainClass.class));
        assertNotNull(result.get(Runnable.class));
        assertNotNull(result.get(TestService.class));
    }

    @Test
    @DisplayName("Instantiate singleton beans -> Should instantiate all singleton beans only once")
    void testSingletonBeans() throws NoSuchMethodException {
        final var tested = new DependencyGraph();

        // Add beans
        tested.addBean(0, SingletonA.class,
                       new ConstructorBeanCreationInformation(SingletonA.class.getConstructor(), true),
                       new Class<?>[]{});
        tested.addBean(0, SingletonB.class,
                       new ConstructorBeanCreationInformation(SingletonB.class.getConstructor(), true),
                       new Class<?>[]{});
        tested.addBean(0, TestSingletons1.class, new ConstructorBeanCreationInformation(
                               TestSingletons1.class.getConstructor(SingletonA.class, SingletonB.class), true),
                       new Class<?>[]{SingletonA.class, SingletonB.class});
        tested.addBean(0, TestSingletons2.class, new ConstructorBeanCreationInformation(
                               TestSingletons2.class.getConstructor(SingletonA.class, SingletonB.class), true),
                       new Class<?>[]{SingletonA.class, SingletonB.class});

        // Instantiate the beans
        final var result = tested.instantiateBeans();
        final TestSingletons1 testSingletons1 = (TestSingletons1) result.get(TestSingletons1.class);
        final TestSingletons2 testSingletons2 = (TestSingletons2) result.get(TestSingletons2.class);
        assertSame(testSingletons1.getSingletonA(), testSingletons2.getSingletonA());
        assertSame(testSingletons1.getSingletonB(), testSingletons2.getSingletonB());
    }

}