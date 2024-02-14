# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

Sequence Diagram
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSgM536HHCkARYGGABBECE5cAJsOAAjYBxQxp8zJgDmUCAFdsMAMRpgVAJ4wASik1IOYKMKQQ0RgO4ALJGBSZEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAel1lKAAdNABvLMoTAFsUABoYXA4ON2hpSpQS4CQEAF9MCnDYELC2SQ4oqBs7HygACiKoUoqqpVr6xubWgEpO1nYOGF6hEXEBwZhNFDAAVWzJ7Jm13bEJKW3QtSiyAFEAGVe4JJgpmZgAGY6Eq-bKYW77B6BXpdfqcKJoXQIBDraibR4wCH3GpREDDYQoc6US7FYBlSrVBZQaQ3OSQmoY54wACSADk3pYfn8ybNKXVqUsWggWaykvFQbRYeidnTsYc8SgCaJdGAPCTpjzaXs5Yz5FE2RyuVceZVgCqPEkIABrdAisUwM2q8Gyg7bGEbAZRR0W63oVFwrbQ0KwyIO82Wm1of2UN2hfzoMBRABMAAYU3l8t6I+gOuhpBptHoDIZoPxjjB3hBbK5DJ5vL540Fg6xQ3FEqkMsoas40BnueS5jV+dIOiGoCEpZ6EFWkGh1TNynz6mtJ5wQliDhFjmcLv2UFq7gcQs83p9vhKNWVAcCL-7NgAeaEbqQRKjAZDSIlQO8Ddcul9vh+BKxPMw7ziaS7Uge9IcMeeoAGqiO8zICKISSvIOVLSNeEAglMP6cI+gTPjigFINIIFDvUBGwcEJGDAqSrmuBZTQXKcERAarycheC5hqq2auGy9rejRRH0REWa+lGY5EaugxSZGfjIAmcktlAESpumBSKTmmB5gWOj6EY2goLalaaHozC1l4PjKQEzDut0UTRAIHyvOhaTpF2HA9nkukyepE4evCwwlBANDKqq4wBSuIW0RJYURSgUVqrFzrakewTPJYrwALLxPBGEBZUe4wAAYpY8R5fxHhiU+-44hwuhytGUBqWinrNa1jZgB1NAaVp+loPmWhGcWwzYe8owwAA4jyWw2fW9kJhiY4ubNHleZoPL+eG0ltcFnXwsg9jzWUHCpTF+2RnFx0JY1gzTWdC1Xel9EcaeXw-DtF3RGkADqzJJAAErVgn1cRj1bgt-1tf1mww39KQrRg-WRFpGa-Zw-25iNhlFkY2C6FA2AICocD4j4c08u4tkNipTZ9K2CTJNtu0mDd6BYzyrI8qOQXBPJESMT450oNdAnSeU2N86xNFHQGItUyg4vjLLmoK3R0PbuLHCSz6t0ZYeUife557Yxw-0VVVNUBTAkMSZbcPyYriOiygrIoG4z1gOrsMpOUaDe+Ld0Bn+mUvsoYBe244u+-7yNsVlzxcTxztpPD0LC9jKGo312fqZpaY82UKF46NhbGYYZgoMiEBuDAABSECzjTV6GPICCgFa+drUXMSxKcHbpNje1S5GGa9XAEDTlAlS5wIAvdG7noAFat2gaslYv5TT7P0Bh5sEcm6R77kQS4soYnKAocnpvZXqX0W7Di8o-J4k66-PIoe-8Wr-CEAB8oDMkum+VoChybjH3nPGW38y4CBSEfX82tI44n0LIMWPIb5WzfuUd6j0OJpx+vA2+iCs6BGFt1A4+d0aDTTMNSu40TKOAsIgRUsBgDYDJoQJwLg6bLV6v3ZyMQ3KfE8hkNQh0hbxQiNIOuJwUACHkOMZBa5UGn0GPI8mPhlGqONjBDiuUCpFRgEhd4tVHbQ2oVIChVCWo0N6nQ4uKZGGYCAA
