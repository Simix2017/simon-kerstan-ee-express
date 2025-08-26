/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.web;

import de.simonkerstan.ee.core.ApplicationContext;
import jakarta.servlet.Servlet;

/**
 * Web server to be started in the application bootstrapping code.
 */
public interface Server {

    /**
     * Create a new server instance using the default Jakarta EE servlet implementation.
     *
     * @param applicationContext Application context to be used by the server implementation
     * @return Server instance
     */
    static Server create(ApplicationContext applicationContext) {
        return ServerFactory.getInstance()
                .createServer(applicationContext);
    }

    /**
     * Add a default servlet provided by the server used for serving static resources. Per default, only the JAX-RS
     * servlet implementation is added at the path {@code /api/*}.
     */
    void addDefaultServlet();

    /**
     * Add a servlet to the server.
     *
     * @param servletMapping URL mapping for the servlet
     * @param servletName    Name of the servlet
     * @param servlet        Servlet implementation
     */
    void addServlet(String servletMapping, String servletName, Servlet servlet);

    /**
     * Start the server.
     */
    void start();

    /**
     * Wait for the server to stop.
     */
    void waitWhileRunning();

    /**
     * Stop the server.
     */
    void stop();

}
