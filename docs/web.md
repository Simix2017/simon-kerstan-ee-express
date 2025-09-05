# EE Express Web module

Web Jakarta EE runtime with a Servlet container and a JAX-RS runtime. Different to the initialization of standalone
applications, a server must be created with an initialized application context. This server can be started after adding
custom servlets and filters. For more information, have a look at
the [web application getting started guide](../README.md#web-application).

JAX-RS is automatically enabled with all available JAX-RS resources inside the bootstrap packages (and their recursive
subpackages) for path `/api/**`.

Currently, Tomcat is used as the embedded web server and Jersey is used as the JAX-RS implementation.

## Configuration

This module provides the following configuration parameters:

| Parameter     | Description        | Default value |
|---------------|--------------------|---------------|
| `server.port` | Server port (HTTP) | `8080`        |

For more information about how to set the configuration parameters, see the [configuration API](core/configuration.md).