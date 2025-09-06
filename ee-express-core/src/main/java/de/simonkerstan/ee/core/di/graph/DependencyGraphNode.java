/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di.graph;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Node for a dependency graph.
 */
@RequiredArgsConstructor
final class DependencyGraphNode {

    @Getter
    private final int priority;
    @Getter
    private final Class<?> type;
    @Getter
    private final BeanCreationInformation beanCreationInformation;
    private final List<DependencyGraphNode> dependencies = new LinkedList<>();

    /**
     * Add a dependency to the node.
     *
     * @param dependency Dependency to be added
     */
    public void addDependency(DependencyGraphNode dependency) {
        this.dependencies.add(dependency);
    }

    /**
     * Get all dependencies of the node.
     *
     * @return Dependencies
     */
    public List<DependencyGraphNode> getDependencies() {
        return Collections.unmodifiableList(this.dependencies);
    }

    /**
     * Remove a dependency from the node.
     *
     * @param dependency Dependency to be removed
     */
    public void removeDependency(DependencyGraphNode dependency) {
        this.dependencies.remove(dependency);
    }

    /**
     * Remove a dependency from the node if it exists.
     *
     * @param dependency Dependency to be removed
     * @return {@code true} if the dependency was removed, {@code false} otherwise
     */
    public boolean removeDependencyIfExisting(DependencyGraphNode dependency) {
        final var iterator = this.dependencies.iterator();
        while (iterator.hasNext()) {
            final var next = iterator.next();
            if (next == dependency) {
                // We check for identity because we know the exact object
                iterator.remove();
                return true;
            }
        }

        return false;
    }

}
