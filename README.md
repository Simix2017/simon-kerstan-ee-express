# Simon Kerstan EE Express

Simon Kerstan EE Express is a lightweight and minimal Jakarta EE runtime for Java. It does not require any tooling and
can easily be used.

Copyright (c) 2025 Simon Kerstan

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

## Contributing

If you want to contribute to this project, please read the [contributing guidelines](CONTRIBUTING.md).

## Compatibility

This project is compatible with Java 17 and above. Currently, the framework is based on
[Jakarta EE Platform 11](https://jakarta.ee/specifications/platform/11/).

## Build

This project uses [Gradle](https://gradle.org/) as a build tool. To build the project, you can use the following
command: `./gradlew clean build`.

Because Maven Central requires signed builds, the `signing` plugin in Gradle is activated. To sign the artifacts, you
need to provide your GPG key and password as Gradle properties. You could use the following command to sign the
artifacts: `./gradlew clean build -Psigning.keyId=<KEYID> -Psigning.password= \
-Psigning.secretKeyRingFile=<KEYRING_FILE>`.

For snapshots, the signing plugin is deactivated.

## Modules

Modules which are defined as "in development" are not yet available on Maven Central, but you can build them yourself.

### EE Express

_In development_

Web module | [Maven Central](https://central.sonatype.org/artifact/de.simonkerstan/ee-express)

This project bundles all web modules of this framework into one dependency. You can use this dependency instead of a
list of all web modules in your `build.gradle` file.

### EE Express Core

Base
module | [Module documentation](docs/core.md) | [Maven Central](https://central.sonatype.com/artifact/de.simonkerstan/ee-express-core)

Core Jakarta EE runtime with Jakarta EE Dependency Injection and configuration APIs. This can be used for web and
non-web applications and has APIs for bootstrapping applications. Every other module of the framework is based on this
module.

Besides core APIs from Jakarta EE, this module also provides logging support.

In the future, the module will also provide a full CDI implementation.

The following Jakarta EE (and own) APIs are available inside this module:

- [Jakarta Dependency Injection 2.0](https://jakarta.ee/specifications/dependency-injection/2.0/)
- [Jakarta JSON Binding 3.0](https://jakarta.ee/specifications/jsonb/3.0/)
- [Jakarta JSON Processing 2.1](https://jakarta.ee/specifications/jsonp/2.1/)
- [Jakarta XML Binding 4.0](https://jakarta.ee/specifications/xml-binding/4.0/)
- [SLF4J](https://slf4j.org/)
- [Configuration API](docs/core/configuration.md)

### EE Express Persistence

_In development_

Universal
module | [Module documentation](docs/persistence.md) | [Maven Central](https://central.sonatype.com/artifact/de.simonkerstan/ee-express-validation)

This module provides persistence with relational databases and distributed cache support (Redis).

The following Jakarta EE APIs are available inside this module:

- [Jakarta Data 1.0](https://jakarta.ee/specifications/data/1.0/)
- [Jakarta Persistence 3.2](https://jakarta.ee/specifications/persistence/3.2/)

### EE Express Validation

Universal
module | [Module documentation](docs/validation.md) | [Maven Central](https://central.sonatype.com/artifact/de.simonkerstan/ee-express-validation)

Validation API for Jakarta EE. A configured `Validator` is automatically provided in the CDI context.

The following Jakarta EE APIs are available inside this module:

- [Jakarta Validation 3.1](https://jakarta.ee/specifications/bean-validation/3.1/)

### EE Express Web

Web
module | [Module documentation](docs/web.md) | [Maven Central](https://central.sonatype.com/artifact/de.simonkerstan/ee-express-web)

Web Jakarta EE runtime with a Servlet container and a JAX-RS runtime. Different to the initialization of standalone
applications, a server must be created with an initialized application context. This server can be started after adding
custom servlets and filters. For more information, have a look at
the [web application getting started guide](#web-application).

JAX-RS is automatically enabled with all available JAX-RS resources inside the bootstrap packages (and their recursive
subpackages) for path `/api/**`.

Currently, Tomcat is used as the embedded web server and Jersey is used as the JAX-RS implementation.

The following Jakarta EE APIs are available inside this module:

- [Jakarta Servlet 6.1](https://jakarta.ee/specifications/servlet/6.1/)
- [Jakarta RESTful Web Services 4.0](https://jakarta.ee/specifications/restful-ws/4.0/)

## Getting started

### Standalone application

If you want to start a standalone application, you can use the EE Express Core module. You can use a `build.gradle`
file like this:

```groovy
plugins {
    id 'java'
    id('io.freefair.lombok') version '8.14.2'
}

group = 'com.example'
version = '0.1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'de.simonkerstan:ee-express-core:0.1.0-SNAPSHOT'

    testImplementation 'org.junit.jupiter:junit-jupiter:5.13.4'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
    testImplementation 'org.mockito:mockito-core:5.19.0'
}

tasks.withType(Test).configureEach {
    def mockitoAgent = configurations.testRuntimeClasspath.find {
        it.name.contains('mockito-core')
    }
    if (mockitoAgent) {
        jvmArgs = ["-javaagent:${mockitoAgent.absolutePath}", '--add-opens=java.base/java.lang=ALL-UNNAMED', '--add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED']
    } else {
        jvmArgs = ['--add-opens=java.base/java.lang=ALL-UNNAMED', '--add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED']
    }
}

test {
    useJUnitPlatform()
}
```

and a `Main.java` file like this:

```java
package com.example;

import de.simonkerstan.ee.core.ApplicationContext;
import de.simonkerstan.ee.core.EeExpressApplication;

public class Main {

    public static void main(String[] args) {
        final ApplicationContext context = EeExpressApplication.initialize(args, "com.example");
        EeExpressApplication.run(context);
    }

}
```

In the given example, all classes inside the `com.example` package (and all sub-packages) are bootstrapped by the CDI
runtime. In all scanned classes, there must be exactly one class annotated with `@MainApplication`. This class must
implement the `Runnable` interface. It is used like the `main` method in a normal Java application to contain the
business logic of the application. Inside this class, you can inject any beans of the CDI context by using the `@Inject`
annotation. An example of a `MainApplication` class could look like this (instead you could also use the class with the
`main` method):

```java
package com.example;

import de.simonkerstan.ee.core.annotations.MainApplication;
import jakarta.inject.Inject;

@MainApplication
public class Application implements Runnable {

    private final BusinessService1 service1;
    private final BusinessService2 service2;

    @Inject
    public Application(BusinessService1 service1, BusinessService2 service2) {
        this.service1 = service1;
        this.service2 = service2;
    }

    @Override
    public void run() {
        // ...
    }

}
```

### Web application

If you want to start a web application, you can use the EE Express Web module. You can use a `build.gradle` file
like this (see [Standalone application](#standalone-application) for the standalone application example):

```groovy
//...

dependencies {
    implementation 'de.simonkerstan:ee-express-core:0.1.0-SNAPSHOT'
    implementation 'de.simonkerstan:ee-express-web:0.1.0-SNAPSHOT'
    // or use only the following line if you want to include all modules of Simon Kerstan EE Express
    // implementation 'de.simonkerstan:ee-express:0.1.0-SNAPSHOT'

    //...
}

//...
```

and a `Main.java` file like this:

```java
package com.example;

import de.simonkerstan.ee.core.ApplicationContext;
import de.simonkerstan.ee.core.EeExpressApplication;
import de.simonkerstan.ee.web.Server;

public class Main {

    public static void main(String[] args) {
        final ApplicationContext context = EeExpressApplication.initialize(args, "com.example");
        final Server server = Server.create(context);
        // server.addDefaultServlet();
        // server.addServlet("/myServlet/*", "MyServlet", new MyServlet());
        // ...
        server.start();
        server.waitWhileRunning();
    }

}
```

### Configuration

Configuration handling is implemented in the EE Express Core module. You can use the `Configuration` class to
retrieve configuration values. Different sources are used to retrieve the configuration values.

All framework modules have their own configuration options. You can find the configuration options for each module
inside the documentation of the module.

For more information, please see the [configuration API documentation](docs/core/configuration.md).
