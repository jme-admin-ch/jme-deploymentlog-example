# JME DeploymentLog Example

This example project shows how to assemble a runnable deployment-log service using the
[jeap-deploymentlog-service-instance](https://github.com/jeap-admin-ch/jeap-deploymentlog-service) starter.
The starter contains all the necessary components (REST API, persistence, security) to set up a deployment-log
service instance; this project wires it up with a minimal `pom.xml`/configuration and a PostgreSQL database,
without any custom application code of its own.

## What this example demonstrates

A deployment log records, per system and environment, which component version was deployed, when, by whom, and
which artifact was used — used to answer "what is running where" questions. This example shows the minimal setup
needed to run such a service:

- Depending on `jeap-deploymentlog-service-instance` to get the deployment-log REST API and persistence out of the box.
- `application.yml` configuring the service name, database schema, and enabling Swagger.
- A PostgreSQL database provisioned via `docker/docker-compose.yml`.
- An integration test (`DeploymentLogExampleIT`) that starts the service and exercises its API end to end:
  registering a new deployment via `PUT /api/deployment/{id}` (with basic-auth `write` credentials), then reading
  it back via `GET /api/deployment/{id}` (with basic-auth `read` credentials).

## Changes

This library is versioned using [Semantic Versioning](http://semver.org/) and all changes are documented in
[CHANGELOG.md](./CHANGELOG.md) following the format defined in [Keep a Changelog](http://keepachangelog.com/).

## Prerequisites

To use this project, ensure you have the following installed:

1. **Java Development Kit (JDK)**: Version 25.
2. **Docker**: For running the required infrastructure.

**Note:** Use the provided maven wrapper to build and run the project.

## Getting started

### Infrastructure

Before the examples can be started the infrastructure has to be started using docker. This starts the PostgreSQL
database the deployment-log service persists its data to:

```shell
docker-compose -f docker/docker-compose.yml up
```

### Build

The project itself can be built with a simple

```shell
./mvnw install
```

### Start

Then the project can be started using

```shell
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

### Trying it out

Once the application is running, open the Swagger UI at
[http://localhost:8080/jme-deploymentlog-service/swagger-ui.html](http://localhost:8080/jme-deploymentlog-service/swagger-ui.html)
to explore the deployment-log API — registering a deployment (`PUT /api/deployment/{id}`) requires basic-auth
credentials with a `write` role, reading one back (`GET /api/deployment/{id}`) requires a `read` role. See
`DeploymentLogExampleIT` for a runnable end-to-end example of both calls.

## Profiles

* **application-local:** Contains all configurations for running the application locally.

## Note

This repository is part of the open source distribution of JME. See [github.com/jme-admin-ch/jme](https://github.com/jme-admin-ch/jme)
for more information.

## License

This repository is Open Source Software licensed under the [Apache License 2.0](./LICENSE).
