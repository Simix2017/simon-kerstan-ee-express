/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.web.tomcat;

import de.simonkerstan.ee.core.ApplicationContext;
import de.simonkerstan.ee.core.configuration.MissingPropertyException;
import de.simonkerstan.ee.web.Server;
import de.simonkerstan.ee.web.hk2.EeExpressHk2DependencyBinder;
import jakarta.servlet.Servlet;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Tomcat server implementation.
 */
@Slf4j
public class TomcatServer implements Server {

    private final Tomcat tomcat;
    private final Context context;

    @Getter
    private int port = 0;

    /**
     * Create a new Tomcat server instance.
     *
     * @param applicationContext Application context to be used by the server implementation
     * @throws IOException              If the temporary directory cannot be created
     * @throws MissingPropertyException If the required server port is missing
     */
    public TomcatServer(ApplicationContext applicationContext) throws IOException, MissingPropertyException {
        this(applicationContext.getConfiguration()
                     .getRequiredPropertyValue("server.port", Integer.class), applicationContext);
    }

    TomcatServer(int port, ApplicationContext applicationContext) throws IOException {
        // Initialize Tomcat (embedded web server)
        this.tomcat = new Tomcat();
        this.tomcat.setBaseDir(Files.createTempDirectory("simon_kerstan_ee_express")
                                       .toString());
        this.tomcat.setPort(port);

        final var docBase = new File(".").getAbsolutePath();
        this.context = this.tomcat.addContext("", docBase);

        // Register Jersey for JAX-RS
        final var resourceConfig = new ResourceConfig();
        resourceConfig.packages(applicationContext.getBootstrapPackages());
        resourceConfig.register(new EeExpressHk2DependencyBinder(applicationContext));

        final var servletContainer = new ServletContainer(resourceConfig);
        this.addServlet("/api/*", "JaxRsServlet", servletContainer);

        // Register shutdown handling
        Runtime.getRuntime()
                .addShutdownHook(new Thread(() -> {
                    try {
                        this.tomcat.stop();
                    } catch (LifecycleException e) {
                        log.warn("Error stopping Tomcat (in shutdown hook)", e);
                    }
                }));
    }

    @Override
    public void addDefaultServlet() {
        final var servlet = new DefaultServlet();
        this.addServlet("/*", "DefaultServlet", servlet);
    }

    @Override
    public void addServlet(String servletMapping, String servletName, Servlet servlet) {
        this.tomcat.addServlet("", servletName, servlet);
        this.context.addServletMappingDecoded(servletMapping, servletName);
    }

    @Override
    public void start() {
        try {
            this.tomcat.getServer()
                    .start();
            this.tomcat.getConnector()
                    .start();
            this.port = this.tomcat.getConnector()
                    .getLocalPort();
        } catch (LifecycleException e) {
            throw new RuntimeException("Cannot start Tomcat server", e);
        }
    }

    @Override
    public void waitWhileRunning() {
        this.tomcat.getServer()
                .await();
    }

    @Override
    public void stop() {
        try {
            this.tomcat.stop();
        } catch (LifecycleException e) {
            throw new RuntimeException("Cannot stop Tomcat server", e);
        }
    }

}
