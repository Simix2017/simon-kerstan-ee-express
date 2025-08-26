/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.web.hk2;

import de.simonkerstan.ee.core.ApplicationContext;
import lombok.RequiredArgsConstructor;
import org.glassfish.jersey.internal.inject.AbstractBinder;

/**
 * Dependency binder for EE Express Core CDI implementation (used by HK2).
 */
@RequiredArgsConstructor
public class EeExpressHk2DependencyBinder extends AbstractBinder {

    private final ApplicationContext applicationContext;

    @Override
    protected void configure() {
        // Register all known beans (that are not JAX-RS controller classes)
        this.applicationContext.getBeans()
                .entrySet()
                .stream()
                .filter(JaxRsClassChecker::isNoJaxRsClass)
                .forEach(beanMapping -> bind(beanMapping.getValue()).to(beanMapping.getKey()));
    }

}
