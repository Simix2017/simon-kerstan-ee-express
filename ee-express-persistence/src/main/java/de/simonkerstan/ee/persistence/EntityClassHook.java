/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.persistence;

import de.simonkerstan.ee.core.clazz.ClassHook;
import jakarta.persistence.Entity;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class hook for finding entities.
 * <p>
 * FOR INTERNAL USE ONLY. THE API CAN CHANGE AT ANY TIME.
 */
public class EntityClassHook implements ClassHook {

    private final List<Class<?>> entities = new LinkedList<>();

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends Annotation>[] getClassAnnotations() {
        return new Class[]{Entity.class};
    }

    @Override
    public void processClass(Class<?> clazz, Class<? extends Annotation> annotation, Annotation annotationInstance) {
        // Only one annotation possible, so we can ignore the annotation parameter
        this.entities.add(clazz);
    }

    public List<Class<?>> getEntities() {
        return Collections.unmodifiableList(this.entities);
    }

}
