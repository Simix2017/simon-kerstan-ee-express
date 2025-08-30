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

## Projects

### EE Express

This project bundles all modules of this framework into one dependency.

### EE Express Core

Core Jakarta EE runtime with Jakarta EE CDI and configuration APIs. This can be used for non-web applications and has
APIs for bootstrapping the application. Every other module inside the framework is based on this module.

Besides core APIs from Jakarta EE, this module also provides logging via SLF4J (which uses Logback Classic behind the
scenes).

The CDI implementation does only support dependency injection via annotations currently and does not support the full
CDI specification.

The following Jakarta EE APIs are available inside this module:

- [Jakarta Dependency Injection 2.0](https://jakarta.ee/specifications/dependency-injection/2.0/)
- [Jakarta JSON Binding 3.0](https://jakarta.ee/specifications/jsonb/3.0/)
- [Jakarta JSON Processing 2.1](https://jakarta.ee/specifications/jsonp/2.1/)
- [Jakarta XML Binding 4.0](https://jakarta.ee/specifications/xml-binding/4.0/)

### EE Express Validation

Validation API for Jakarta EE. A configured `Validator` is automatically provided in the CDI context.

The following Jakarta EE APIs are available inside this module:

- [Jakarta Validation 3.1](https://jakarta.ee/specifications/bean-validation/3.1/)

### EE Express Web

Web Jakarta EE runtime with a Servlet and JAX-RS runtime. Currently, Tomcat is used as the embedded web server and
Jersey is used as the JAX-RS implementation. If using this module, you do not need to bootstrap the application
yourself. Instead, you can just start the server. Please note that JAX-RS is automatically enabled for path `/api/**`.

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
    id('io.freefair.lombok') version '8.14'
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
