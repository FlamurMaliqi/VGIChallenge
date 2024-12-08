# VGIBackend

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

You can then execute your native executable with: `./build/VGIBackend-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/gradle-tooling>.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

## JSONs:

### ConnectionRequest Example
```json
{
  "fromStopName": "Christoph-Scheiner-Gymnasium",
  "fromCoordinates": {
    "latitude": 48.7602430,
    "longitude": 11.4224340
  },
  "toStopName": "Humboldtstraße",
  "toCoordinates": {
    "latitude": 48.7571210,
    "longitude": 11.4004590
  }
}
```

### Connection Example
```json
{
    "duration": "00:13:00",
    "isBlocked": false,
    "from": {
        "stopName": "Christoph-Scheiner-Gymnasium",
        "departure": "2024-11-15T11:00:00",
        "arrival": null,
        "coordinates": {
            "latitude": 48.7602430,
            "longitude": 11.4224340
        }
    },
    "to": {
        "stopName": "Humboldtstraße",
        "departure": null,
        "arrival": "2024-11-15T11:13:00",
        "coordinates": {
            "latitude": 48.7571210,
            "longitude": 11.4004590
        }
    },
    "connectionSegments": [
        {
            "routeShortName": "30",
            "tripHeadsign": "Lenting",
            "expectedOccupation": 70,
            "isBlocked": false,
            "fromStop": {
                "stopName": "Christoph-Scheiner-Gymnasium",
                "departure": "2024-11-15T11:00:00",
                "arrival": null,
                "coordinates": {
                    "latitude": 48.7602430,
                    "longitude": 11.4224340
                }
            },
            "toStop": {
                "stopName": "Universität (Kreuztor)",
                "departure": null,
                "arrival": "2024-11-15T11:02:00",
                "coordinates": {
                    "latitude": 48.7653800,
                    "longitude": 11.4165920
                }
            }
        },
        {
            "routeShortName": "53",
            "tripHeadsign": "Humboldtstraße",
            "expectedOccupation": 50,
            "isBlocked": false,
            "fromStop": {
                "stopName": "Universität (Kreuztor)",
                "departure": "2024-11-15T11:09:00",
                "arrival": null,
                "coordinates": {
                    "latitude": 48.7653800,
                    "longitude": 11.4165920
                }
            },
            "toStop": {
                "stopName": "Humboldtstraße",
                "departure": null,
                "arrival": "2024-11-15T11:13:00",
                "coordinates": {
                    "latitude": 48.7571210,
                    "longitude": 11.4004590
                }
            }
        }
    ]
}
```

