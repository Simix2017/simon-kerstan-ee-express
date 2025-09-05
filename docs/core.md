# EE Express Core module

Core Jakarta EE runtime with Jakarta EE CDI and configuration APIs. This can be used for web and non-web applications
and has APIs for bootstrapping applications. Every other module of the framework is based on this module.

Besides core APIs from Jakarta EE, this module also provides logging support.

The CDI implementation does only support dependency injection via annotations currently and does not support the full
CDI specification.

## Configuration

TODO: add configuration (e.g., path's to other configuration files) documentation

## Dependency injection

Dependency injection (based
on [Jakarta Dependency Injection 2.0](https://jakarta.ee/specifications/dependency-injection/2.0/)) works without any
configuration. Beans are automatically discovered inside the given bootstrap packages (and their recursive subpackages)
in the application initialization. To be automatically discovered, a bean must have exactly one constructor annotated
with `@javax.inject.Inject` or must be required in another bean with a constructor annotated with
`@javax.inject.Inject`.

## Logging

Logging is provided via [SLF4J](https://slf4j.org/). Behind the scenes, [Logback Classic](https://logback.qos.ch/) is
used which can be configured either manually by the application or by using standard configuration parameters.

## Standalone applications

Standalone applications are Jakarta EE platform-based applications that do not use any web container. Mostly, they are
used for local client applications. You can bootstrap a standalone application by following
the [standalone application getting started guide](../README.md#standalone-application).
