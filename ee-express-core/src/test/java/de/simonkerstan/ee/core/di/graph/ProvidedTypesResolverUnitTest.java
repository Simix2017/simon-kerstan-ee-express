/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.di.graph;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProvidedTypesResolverUnitTest {

    @Test
    @DisplayName("Get provided types from ArrayList -> Should return all provided types")
    void testArrayList() {
        final var result = ProvidedTypesResolver.resolve(ArrayList.class);
        System.out.println("ArrayList (provided types): " + Arrays.toString(result));

        assertEquals(5, result.length);
        assertEquals(Collection.class, result[0]);
        assertEquals(ArrayList.class, result[1]);
        assertEquals(AbstractCollection.class, result[2]);
        assertEquals(AbstractList.class, result[3]);
        assertEquals(List.class, result[4]);
    }

}