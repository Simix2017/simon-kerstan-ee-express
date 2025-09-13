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

## Configuration parameters used to resolve some configuration sources

| Parameter                             | Description                                                                    | Default value |
|---------------------------------------|--------------------------------------------------------------------------------|---------------|
| `core.configuration.properties.files` | Properties files used as configuration file sources (semicolon-separated list) | <empty>       |
| `core.configuration.xml.files`        | XML files used as configuration file sources (semicolon-separated list)        | <empty>       |

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

    @Override
    public Optional<List<String>> getConfigurationSubValues(String propertyName) {
        return Optional.empty();
    }

}
```

### Configured other configuration files

In the
section [Configuration parameters used to resolve some configuration sources](#configuration-parameters-used-to-resolve-some-configuration-sources)
there are configuration parameters that can be used to configure the configuration file sources.

#### Properties files

Properties files are loaded from the configured files and can be used to set configuration properties.

#### XML files

TODO: Add XML support

### Environment variables

Environment variables are loaded by the JVM automatically and can also be used to set configuration properties.
Environment variable names are always uppercase and dots are replaced with underscores. For example, the environment
variable `MY_VALUE` is mapped to the configuration property `my.value`.

### application.properties file in classpath

From the classpath a file named `application.properties` is loaded. This file can be used to set configuration
properties.

## Configuration resolving

Configuration values can be resolved by giving the target type which leads to automatic type conversion. All primitive
types and their respective wrappers (and enums) are supported.

Also, records are supported where the fields are mapped to configuration properties. For example, if the record
`MyRecord` has a field `myValue` and the configuration property `my.value` is set to `123`, the value of `myValue` will
be `123` if the configuration property `my` is resolved as an instance of `MyRecord`.

### Lists and maps of configuration values

Only custom and configuration file sources support lists and maps of configuration values. You can use special methods
of the `Configuration` interface/bean to retrieve lists and maps of configuration values. Different to other types,
lists and maps are combined from all compatible sources. If the same key in a map exists in multiple sources, the
value from the first source is used (just like for any other type).
