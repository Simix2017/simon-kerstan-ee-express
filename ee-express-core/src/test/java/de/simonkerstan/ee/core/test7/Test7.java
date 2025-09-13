/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.core.test7;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Static holder for configuration values.
 */
public class Test7 {

    @Getter
    @Setter
    private static List<String> testList;

    @Getter
    @Setter
    private static Map<String, String> testMap;

    @Getter
    @Setter
    private static String xyzFromPropertiesFile;

}
