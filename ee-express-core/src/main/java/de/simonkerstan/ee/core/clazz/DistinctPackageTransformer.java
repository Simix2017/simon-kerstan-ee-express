/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.clazz;

import java.util.Arrays;

/**
 * Transformer for removing duplicate packages.
 */
final class DistinctPackageTransformer {

    private DistinctPackageTransformer() {
    }

    /**
     * Get a distinct array of (base) packages.
     *
     * @param packages Packages to be processed
     * @return Distinct (base) packages
     */
    public static String[] distinct(String[] packages) {
        final var distinctPackages = Arrays.stream(packages)
                .distinct()
                .toList();
        return distinctPackages.stream()
                // If any of the packages is a subpackage of any other package, filter it out
                .filter(p -> distinctPackages.stream()
                        .noneMatch(prefix -> p.startsWith(prefix) && !p.equals(prefix)))
                .toArray(String[]::new);
    }

}
