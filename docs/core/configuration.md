# Configuration API

This API is used to provide a universal configuration mechanism for both the framework and the application. It was
created because at the time of writing, there was no standard in the Jakarta EE ecosystem for application configuration.

However, there is the [Eclipse MicroProfile Config](https://microprofile.io/specifications/config/) specification that
does not directly relate to the Jakarta EE ecosystem. It has been used as a basis for this API.

## Usage examples

The main object `Configuration` which is used to get configuration properties is automatically injected into your
service class by the framework.

```java
package com.example;

import de.simonkerstan.ee.core.configuration.Configuration;
import de.simonkerstan.ee.core.exceptions.MissingConfigurationPropertyException;
import jakarta.inject.Inject;

import java.util.Optional;

public class MyService {

    private final Configuration configuration;

    @Inject
    public MyService(Configuration configuration) {
        this.configuration = configuration;
    }

    public void doSomething() {
        final Optional<String> myValue = configuration.getPropertyValue("my.value", String.class);
    }

    public void doSomethingElse() throws MissingConfigurationPropertyException {
        final String myValue = configuration.getRequiredPropertyValue("my.value", String.class);
    }

}
```

There are different layers that are used to retrieve the configuration properties. See the sections below for more
details.

## Configuration sources

There are different sources of configuration that are loaded in the following order (the highest priority first):

1. Command line arguments
2. System properties
3. Custom sources
4. Configured other configuration files
5. Environment variables
6. `application.properties` file in classpath

### Command line arguments

Command line arguments are prefixed with `--` and can be used to set configuration properties. There are two possible
ways to use command line arguments:

- `--my.value=xyz` (with equal sign)
- `--my.value xyz` (with two space-separated parameters)

### System properties

System properties are loaded by the JVM automatically and can also be used to set configuration properties.

### Custom sources

Custom sources are custom implementations of the `ConfigurationProvider` interface that are annotated with
`@ConfigurationSource`. They are automatically loaded and bootstrapped by the framework.

See the example below for more details:

```java
package com.example;

import de.simonkerstan.ee.core.annotations.ConfigurationSource;
import de.simonkerstan.ee.core.configuration.ConfigurationProvider;

import java.util.Optional;

@ConfigurationSource
public class MyConfigurationProvider implements ConfigurationProvider {

    @Override
    public Optional<String> getConfigurationValue(String propertyName) {
        if ("my_property".equals(propertyName)) {
            return Optional.of("Example value");
        }

        return Optional.empty();
    }

}
```

### Configured other configuration files

TODO: add handling of configuration files

### Environment variables

Environment variables are loaded by the JVM automatically and can also be used to set configuration properties.
Environment variable names are always uppercase and dots are replaced with underscores. For example, the environment
variable `MY_VALUE` is mapped to the configuration property `my.value`.

### application.properties file in classpath

From the classpath a file named `application.properties` is loaded. This file can be used to set configuration
properties.
