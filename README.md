# spond Project

The project is implementing a simple REST faced API service to provide access to the 
data on nearby asteroids using [NASA Asteroids API](https://api.nasa.gov).

To make as little calls of the original NASA API as possible, each call result is stored 
in the database. The database data using over the NASA API call response if possible.

The service exposes two REST API endpoints

1. `asteroids/` - requires the `start_date` and the `end_date` query parameters to select 10 asteroids that passed the closest to Earth between two user-provided dates
2. `asteroids/max_diameter_yearly` - requires `year` query parameter to select the largest asteroid (estimated diameter) passing Earth during a user-provided year.

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/.

## Running the application in dev mode

**Important**: to get proper access to the NASA API `neo-ws.api-key` property must be setup into the correct value. 
There are two ways how to do that either add `-Dneo-ws.api-key=<secretkey>` to the command line 
or initialize `NEO_WS_API_KEY=<secretkey>` environment variable. 

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/spond-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Known Limitations

- it is possible to extend the project with the SwaggeUI or/and OpenAPI documentation
- the NASA API response parser considering the happy-path data state only, it would be nice to extend it to check non-OK responses and control `null` cases of the JSON tree nodes
- the NASA REST client implements through the vanilla `javax.net.http` package mostly because of the complex response the API produced, it is possible to replace the client with declarative RESTeasy implementation once the proper response parsing method would figure out
- the unit and integration test assertions are quite high-level, more detail assertions would lead to less error-prone code in future
- integration tests requires the external PostgreSQL database running, it might be possible to use `testcontainers` here with more time spend to integration

