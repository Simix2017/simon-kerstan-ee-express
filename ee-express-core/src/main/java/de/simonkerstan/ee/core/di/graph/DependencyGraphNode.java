/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di.graph;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
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
    private final List<DependencyGraphNode> dependencies = new ArrayList<>();

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

}
