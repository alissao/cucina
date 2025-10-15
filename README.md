# cucina

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./gradlew build -Dquarkus.native.enabled=true
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./gradlew build -Dquarkus.native.enabled=true -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/cucina-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/gradle-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- OpenID Connect Client ([guide](https://quarkus.io/guides/security-openid-connect-client)): Get and refresh access tokens from OpenID Connect providers
- SmallRye Fault Tolerance ([guide](https://quarkus.io/guides/smallrye-fault-tolerance)): Build fault-tolerant network services
- SmallRye JWT ([guide](https://quarkus.io/guides/security-jwt)): Secure your applications with JSON Web Token
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC
- Keycloak Authorization ([guide](https://quarkus.io/guides/security-keycloak-authorization)): Policy enforcer using Keycloak-managed permissions to control access to protected resources
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Kotlin ([guide](https://quarkus.io/guides/kotlin)): Write your services in Kotlin
- Micrometer OpenTelemetry Bridge ([guide](https://quarkus.io/guides/telemetry-micrometer-to-opentelemetry)): Micrometer registry implemented by the OpenTelemetry SDK
- OpenID Connect Redis Token State Manager ([guide](https://quarkus.io/guides/security-openid-connect-client)): Store an OpenID Connect token state in a Redis cache datasource
- Hibernate ORM with Panache and Kotlin ([guide](https://quarkus.io/guides/hibernate-orm-panache-kotlin)): Define your persistent model in Hibernate ORM with Panache
- REST Client ([guide](https://quarkus.io/guides/rest-client)): Call REST services
- OpenAPI Generator - REST Server Generator ([guide](https://docs.quarkiverse.io/quarkus-openapi-generator/dev/index.html)): Provides personalized code generation to get started in a Server project 
- Apache Kafka Client ([guide](https://quarkus.io/guides/kafka)): Connect to Apache Kafka with its native API
- YAML Configuration ([guide](https://quarkus.io/guides/config-yaml)): Use YAML to configure your Quarkus application
- Java Flight Recorder (JFR) ([guide](https://quarkus.io/guides/jfr)): Monitor your applications with Java Flight Recorder
- Vault ([guide](https://quarkiverse.github.io/quarkiverse-docs/quarkus-vault/dev/index.html)): Store your credentials securely in HashiCorp Vault
- SmallRye Health ([guide](https://quarkus.io/guides/smallrye-health)): Monitor service health
- Micrometer metrics ([guide](https://quarkus.io/guides/micrometer)): Instrument the runtime and your application with dimensional metrics using Micrometer.
- Hibernate Validator ([guide](https://quarkus.io/guides/validation)): Validate object properties (field, getter) and method parameters for your beans (REST, CDI, Jakarta Persistence)
- OpenID Connect ([guide](https://quarkus.io/guides/security-openid-connect)): Verify Bearer access tokens and authenticate users with Authorization Code Flow
- Liquibase ([guide](https://quarkus.io/guides/liquibase)): Handle your database schema migrations with Liquibase

## Provided Code

### YAML Config

Configure your application with YAML

[Related guide section...](https://quarkus.io/guides/config-reference#configuration-examples)

The Quarkus application configuration is located in `src/main/resources/application.yml`.

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)


[Related Hibernate with Panache in Kotlin section...](https://quarkus.io/guides/hibernate-orm-panache-kotlin)

### REST Client

Invoke different services through REST with JSON

[Related guide section...](https://quarkus.io/guides/rest-client)

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

### SmallRye Health

Monitor your application's health using SmallRye Health

[Related guide section...](https://quarkus.io/guides/smallrye-health)
