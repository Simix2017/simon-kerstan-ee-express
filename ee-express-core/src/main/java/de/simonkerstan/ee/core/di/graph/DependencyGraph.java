/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di.graph;

import de.simonkerstan.ee.core.exceptions.BeanInstantiationException;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Graph with all beans and their dependencies.
 */
@Slf4j
public final class DependencyGraph {

    /**
     * Map of all nodes in the graph. (class name -> node)
     */
    private final Map<String, DependencyGraphNode> nodes = new HashMap<>();
    /**
     * Map of all classes with a default constructor (type -> implementing class).
     */
    private final Map<Class<?>, Class<?>> defaultConstructorClasses = new HashMap<>();
    /**
     * Map of all unresolved dependencies. (class name -> list of unsatisfied consumers)
     */
    private final Map<String, List<DependencyGraphNode>> unresolvedDependencies = new HashMap<>();

    /**
     * Add a bean to the graph. The graph will automatically resolve all possible types that could be provided by
     * the bean.
     *
     * @param priority                Priority of the bean
     * @param type                    Type of the bean
     * @param beanCreationInformation Bean information
     * @param dependencies            Dependencies of the bean
     */
    public void addBean(int priority, Class<?> type, BeanCreationInformation beanCreationInformation,
                        Class<?>[] dependencies) {
        // TODO: Check if bean already exists
        Arrays.stream(ProvidedTypesResolver.resolve(type))
                // Add all resolved types to the graph
                .forEach(resolvedType -> this.insertOneBeanType(priority, resolvedType, beanCreationInformation, true));
    }

    /**
     * Add a class with a default constructor to the list of classes with a default constructor.
     *
     * @param clazz      Class with a default constructor
     * @param interfaces Directly implemented interfaces of the given class
     */
    public void addDefaultConstructorClass(Class<?> clazz, Class<?>[] interfaces) {
        this.defaultConstructorClasses.put(clazz, clazz);
        Arrays.stream(interfaces)
                .forEach(directInterface -> this.defaultConstructorClasses.put(directInterface, clazz));
    }

    /**
     * Test whether the graph has unresolved dependencies.
     *
     * @return {@code true} if there are unresolved dependencies, {@code false} otherwise.
     */
    public boolean hasUnresolvedDependencies() {
        return !this.unresolvedDependencies.isEmpty();
    }

    /**
     * Instantiate all beans in the graph.
     *
     * @return Map of instantiated beans
     * @throws BeanInstantiationException If instantiation fails for one of the beans or dependencies are unresolvable
     */
    public Map<Class<?>, Object> instantiateBeans() throws BeanInstantiationException {
        if (this.hasUnresolvedDependencies()) {
            // Not all dependencies are resolvable after scanning all classes for @Inject.
            // There could be beans with a default constructor which could be without any annotation. We must now try
            // to instantiate them.
            this.instantiateBeansUsingDefaultConstructor();

            if (this.hasUnresolvedDependencies()) {
                // There are still unresolved dependencies, so we can only throw an exception
                log.error("Missing instantiated beans: {}", this.unresolvedDependencies.keySet());
                throw new BeanInstantiationException("Cannot instantiate beans due to unresolvable dependencies");
            }
        }

        // Create a copy of all nodes to allow removing already instantiated beans
        final Map<String, DependencyGraphNode> allNodes = new HashMap<>(this.nodes);

        // Create the result map
        final Map<Class<?>, Object> beans = new HashMap<>();

        /*
        A special algorithm is used to instantiate all beans:
        - Make a copy of the graph
        - Get all remaining nodes
        - Pick the first one and instantiate it with all dependencies recursively
            - Do this by using the first one as root of a dependency tree
        - Remove all instantiated beans from the graph copy
        - Repeat until no more nodes are left
         */

        // Create a list of all remaining nodes that must be processed
        List<DependencyGraphNode> remainingNodes;
        while (!allNodes.isEmpty()) {
            remainingNodes = new ArrayList<>(allNodes.values());
            final var firstOne = remainingNodes.get(0);
            final Queue<DependencyGraphNode> queue = new LinkedList<>();
            queue.add(firstOne);

            // Traverse the dependency tree
            DependencyGraphNode node;
            while ((node = queue.poll()) != null) {
                // Check if the bean has all dependencies instantiated already
                boolean hasAllDependenciesInstantiated = true;
                for (final var dep : node.getDependencies()) {
                    if (!beans.containsKey(dep.getType())) {
                        // Dependency is not yet instantiated
                        hasAllDependenciesInstantiated = false;
                        queue.add(dep);
                    }
                }

                if (hasAllDependenciesInstantiated) {
                    // Instantiate bean
                    final var constructionParams = Arrays.stream(node.getBeanCreationInformation()
                                                                         .getDependencies())
                            // Get instantiated dependency beans
                            .map(beans::get)
                            .toArray();

                    // Add the bean to the result map
                    final var bean = node.getBeanCreationInformation()
                            .createBean(constructionParams);
                    beans.put(node.getType(), bean);

                    // Remove the instantiated bean from the graph copy
                    allNodes.remove(node.getType()
                                            .getName());
                } else {
                    // Bean has unsatisfied dependencies
                    queue.add(node);
                }
            }
        }
        // TODO: check for cyclic dependencies

        return beans;
    }

    private void insertOneBeanType(int priority, Class<?> type, BeanCreationInformation beanCreationInformation,
                                   boolean removeFromUnresolvedDependencies) {
        final var beanClassName = type.getName();

        // Create a new node for the bean
        final var node = new DependencyGraphNode(priority, type, beanCreationInformation);
        this.nodes.put(beanClassName, node);

        // Check if other beans have this bean as unsatisfied dependency
        final var unsatisfiedConsumers = this.unresolvedDependencies.get(beanClassName);
        if (unsatisfiedConsumers != null) {
            unsatisfiedConsumers.forEach(consumer -> consumer.addDependency(node));
        }
        if (removeFromUnresolvedDependencies) {
            this.unresolvedDependencies.remove(beanClassName);
        }

        final var dependencyNames = Arrays.stream(beanCreationInformation.getDependencies())
                .map(Class::getName)
                .toList();
        // Add all dependencies to the new node
        dependencyNames.stream()
                .filter(this.nodes::containsKey)
                .map(this.nodes::get)
                .forEach(node::addDependency);
        // Update the unresolved dependencies list
        dependencyNames.stream()
                .filter(dependencyName -> !this.nodes.containsKey(dependencyName))
                .forEach(dependencyName -> this.unresolvedDependencies.computeIfAbsent(dependencyName,
                                                                                       key -> new ArrayList<>())
                        .add(node));
    }

    /**
     * Try to instantiate all unresolved beans using their default constructor. If it does not exist, we have no chance
     * to instantiate them, and there will be left-overs.
     */
    private void instantiateBeansUsingDefaultConstructor() {
        final var iterator = this.unresolvedDependencies.entrySet()
                .iterator();
        Map.Entry<String, List<DependencyGraphNode>> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            final var beanTypeName = entry.getKey();
            this.defaultConstructorClasses.entrySet()
                    .stream()
                    .filter(entry1 -> beanTypeName.equals(entry1.getKey()
                                                                  .getName()))
                    .findAny()
                    .ifPresent(entry1 -> {
                        // Class with a default constructor for this bean exists
                        try {
                            this.insertOneBeanType(0, entry1.getKey(), new ConstructorBeanCreationInformation(
                                    entry1.getValue()
                                            .getDeclaredConstructor(), false), false);
                            iterator.remove();
                        } catch (NoSuchMethodException e) {
                            // Impossible
                        }
                    });
        }
    }

}
