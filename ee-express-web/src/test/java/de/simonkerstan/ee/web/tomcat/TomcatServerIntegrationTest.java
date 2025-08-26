/*
 * Copyright (c) 2025 Simon Kerstan
 * SPDX-License-Identifier: MIT
 */

package de.simonkerstan.ee.web.tomcat;

import de.simonkerstan.ee.core.EeExpressApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class TomcatServerIntegrationTest {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final TomcatServer tested;
    private final int port;

    TomcatServerIntegrationTest() throws IOException {
        final var applicationContext = EeExpressApplication.initialize(new String[]{},
                                                                       "de.simonkerstan.ee.web.tomcat.test");
        tested = new TomcatServer(0, applicationContext);
        tested.start();
        port = tested.getPort();
        log.info("Tomcat started on port {}", port);
    }

    @AfterEach
    void stopTomcat() {
        tested.stop();
    }

    @Test
    @DisplayName("Test getting JSON from server -> Should return JSON")
    void testGetJson() throws IOException, InterruptedException {
        final var url = String.format("http://localhost:%d/api/test/json", port);
        final var request = HttpRequest.newBuilder(URI.create(url))
                .GET()
                .header("Accept", "application/json")
                .build();

        final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        final var body = assertResponse(response, 200, "application/json");
        assertEquals("{\"test\":\"test\"}", body);
    }

    @Test
    @DisplayName("Test getting XML from server -> Should return XML")
    void testGetXml() throws IOException, InterruptedException {
        final var url = String.format("http://localhost:%d/api/test/xml", port);
        final var request = HttpRequest.newBuilder(URI.create(url))
                .GET()
                .header("Accept", "application/xml")
                .build();

        final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        final var body = assertResponse(response, 200, "application/xml");
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" " + "standalone=\"yes\"?><testObject2><test>test</test" +
                        "></testObject2>",
                body);
    }

    @Test
    @DisplayName("Test getting a non-existing resource from server -> Should return 404 error page")
    void test404() throws IOException, InterruptedException {
        final var url = String.format("http://localhost:%d/api/non-existing", port);
        final var request = HttpRequest.newBuilder(URI.create(url))
                .GET()
                .header("Accept", "text/html")
                .build();

        final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        final var body = assertResponse(response, 404, "text/html;charset=utf-8");
        assertTrue(body.contains("HTTP Status 404 – Not Found"));
    }

    private String assertResponse(HttpResponse<String> response, int expectedStatusCode, String expectedContentType) {
        final var body = response.body();
        log.info("Response body: {}", body);

        assertEquals(expectedStatusCode, response.statusCode());
        assertEquals(expectedContentType, response.headers()
                .firstValue("Content-Type")
                .orElse(""));
        return body;
    }

}