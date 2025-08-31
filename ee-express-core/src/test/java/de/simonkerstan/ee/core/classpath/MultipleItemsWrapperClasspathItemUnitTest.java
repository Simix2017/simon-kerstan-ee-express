/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.classpath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MultipleItemsWrapperClasspathItemUnitTest {

    private final ClasspathItem testItem1 = mock(ClasspathItem.class);
    private final ClasspathItem testItem2 = mock(ClasspathItem.class);

    private final MultipleItemsWrapperClasspathItem tested = new MultipleItemsWrapperClasspathItem(
            List.of(testItem1, testItem2));

    @Test
    @DisplayName("Resource exists in the second item -> Should return true")
    void testIsResourceExisting() {
        when(testItem1.isResourceExisting("test")).thenReturn(false);
        when(testItem2.isResourceExisting("test")).thenReturn(true);

        assertTrue(tested.isResourceExisting("test"));
    }

    @Test
    @DisplayName("Resource is directory in the first item -> Should return true")
    void testIsDirectory() {
        when(testItem1.isResourceExisting("test")).thenReturn(true);
        when(testItem1.isDirectory("test")).thenReturn(true);

        assertTrue(tested.isDirectory("test"));
    }

    @Test
    @DisplayName("Resource is not existing in any item -> Should throw exception")
    void testIsDirectoryNotExisting() {
        when(testItem1.isResourceExisting("non-existing")).thenReturn(false);
        when(testItem2.isResourceExisting("non-existing")).thenReturn(false);

        final var exception = assertThrows(IllegalArgumentException.class, () -> tested.isDirectory("non-existing"));
        assertTrue(exception.getMessage()
                           .contains("not existing"));
    }

    @Test
    @DisplayName("Get children of all items -> Should return children")
    void testGetChildren() {
        when(testItem1.isResourceExisting("test")).thenReturn(true);
        when(testItem2.isResourceExisting("test")).thenReturn(true);
        when(testItem1.isDirectory("test")).thenReturn(true);
        when(testItem2.isDirectory("test")).thenReturn(true);
        when(testItem1.getChildren("test")).thenReturn(List.of("test1", "test2"));
        when(testItem2.getChildren("test")).thenReturn(List.of("test3"));

        final var expected = List.of("test1", "test2", "test3");
        final var result = tested.getChildren("test");
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    @DisplayName("Get children of all items but only one has children -> Should return these children")
    void testGetChildrenOnlyOneHasChildren() {
        when(testItem1.isResourceExisting("test")).thenReturn(true);
        when(testItem2.isResourceExisting("test")).thenReturn(false);
        when(testItem1.isDirectory("test")).thenReturn(true);
        when(testItem1.getChildren("test")).thenReturn(List.of("test1", "test2"));

        final var expected = List.of("test1", "test2");
        final var result = tested.getChildren("test");
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    @DisplayName("Get children of all items but only one is a directory -> Should return these children")
    void testGetChildrenNoDirectory() {
        when(testItem1.isResourceExisting("test")).thenReturn(true);
        when(testItem2.isResourceExisting("test")).thenReturn(true);
        when(testItem1.isDirectory("test")).thenReturn(true);
        when(testItem2.isDirectory("test")).thenReturn(false);
        when(testItem1.getChildren("test")).thenReturn(List.of("test1", "test2"));

        final var expected = List.of("test1", "test2");
        final var result = tested.getChildren("test");
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    @DisplayName("Get stream from the second item -> Should return stream")
    void testGetResourceAsStream() throws IOException {
        when(testItem1.isResourceExisting("test")).thenReturn(false);
        when(testItem2.isResourceExisting("test")).thenReturn(true);
        when(testItem2.isDirectory("test")).thenReturn(false);
        when(testItem2.getResourceAsStream("test")).thenReturn(InputStream.nullInputStream());

        try (final var is = tested.getResourceAsStream("test")) {
            assertNotNull(is);
        }
    }

    @Test
    @DisplayName("Get stream of a directory -> Should throw exception")
    void testGetResourceAsStreamOfDirectory() {
        when(testItem1.isResourceExisting("directory")).thenReturn(true);
        when(testItem1.isDirectory("directory")).thenReturn(true);

        final var exception = assertThrows(IllegalArgumentException.class, () -> {
            try (final var is = tested.getResourceAsStream("directory")) {
                // Do nothing
            }
        });
        assertTrue(exception.getMessage()
                           .contains("directory"));
    }

}