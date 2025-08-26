/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.web.tomcat.test;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/test")
public class TestController {

    private final TestService testService;

    @Inject
    public TestController(TestService testService) {
        this.testService = testService;
    }


    @GET
    @Path("/json")
    @Produces(MediaType.APPLICATION_JSON)
    public TestObject testJson() {
        return new TestObject(this.testService.test());
    }

    @GET
    @Path("/xml")
    @Produces(MediaType.APPLICATION_XML)
    public TestObject2 testXml() {
        return new TestObject2(this.testService.test());
    }

}
