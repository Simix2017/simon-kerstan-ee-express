/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.web;

import de.simonkerstan.ee.core.ApplicationContext;
import de.simonkerstan.ee.core.exceptions.MissingConfigurationPropertyException;
import de.simonkerstan.ee.web.tomcat.TomcatServer;

import java.io.IOException;

/**
 * Factory for creating a web server.
 */
final class ServerFactory {

    private static final ServerFactory INSTANCE = new ServerFactory();

    private ServerFactory() {
    }

    public static ServerFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Create a new server instance.
     *
     * @param applicationContext Application context to be used by the server implementation
     * @return Server instance
     */
    public Server createServer(ApplicationContext applicationContext) {
        try {
            return new TomcatServer(applicationContext);
        } catch (IOException | MissingConfigurationPropertyException e) {
            throw new RuntimeException("Cannot initialize Tomcat server", e);
        }
    }

}
