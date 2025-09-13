# EE Express Persistence module

This module provides persistence with relational databases and distributed cache support (Redis).

Currently, only the default datasource is supported for relational sources. Multiple distributed cache sources with a
name are supported.

## Configuration

This module provides the following configuration parameters:

| Parameter                                  | Description                                     | Default value | Possible values   |
|--------------------------------------------|-------------------------------------------------|---------------|-------------------|
| `persistence.source.@NAME@.url`            | Datasource URL                                  | -             |                   |
| `persistence.source.@NAME@.username`       | Datasource Username                             | -             |                   |
| `persistence.source.@NAME@.password`       | Datasource Password                             | -             |                   |
| `persistence.source.@NAME@.driverName`     | Datasource driver name (for relational sources) | -             |                   |
| `persistence.source.@NAME@.type`           | Datasource type                                 | -             | RELATIONAL, REDIS |
| `persistence.source.@NAME@.schemaStrategy` | Schema strategy (for relational sources)        | -             | CREATE            |

For more information about how to set the configuration parameters, see the [configuration API](core/configuration.md).

Please note: a datasource named `default` must be configured for the persistence module to work.

## Support for relational databases

Your using application project must include the used JDBC database driver in the runtime classpath. Also, relational
support with Jakarta Data is currently only implemented for the default datasource.

### Available beans for relational sources

In the CDI context, there is a `EntityManagerFactory` and a `StatelessSession` bean available for each configured
datasource.
