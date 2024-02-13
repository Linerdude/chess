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
https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAEooDmSAzmFMARDQVqhFHXyFiwUgBF+wAIIgQKLl0wATeQCNgXFDA3bMmdlAgBXbDADEaYFQCerDt178kg2wHcAFkjAxRFRSAFoAPnJKGigALhgAbQAFAHkyABUAXRgAegt9KAAdNABvfMp7AFsUABoYXDVvaA06lErgJAQAX0xhGJgIl04ePgEhaNF4qFceSgAKcqgq2vq9LiaoFpg2joQASkw2YfcxvtEByLkwRWVVLnj2FDAAVQKFguWDq5uVNQvDbTxMgAUQAMsC4OkYItljAAGbmSrQgqYb5KX5cAaDI5uUaecYiFTxNAWBAIQ4zE74s4qf5o25qeIgab8FCveYw4DVOoNdbNL7ydF3f5GeIASQAciCWFDOdzVo1mq12p0YJL0ilkbQcSMPIIaQZBvSMUyWYEFBYwL53hUuSgBdchX9BqK1VLgTKtUs7XVgJbfOkIABrdBujUwP1W1GChmY0LYyl4-UTIkR-2BkNoCnHJMEqjneORPqUeKRgPB9C9aKULGRYLoMDxABMAAYW8USmWM+geugNCYzJZrDZoNJHjBQRBOGgfP5Aph62Ei9W4olUhlsjl9Gp8R25SteRsND1i1AIjqqcnCShYggp0g0DbvdzD-zs7i9XnRBFjXdYo8XjefcHR+O4IlFEFwUhL1YQRCAkUWd9dVOFMUAAHnjX9GSoYBkA0dkoCQy8vxUH8YxNHC8NZJI1iPJ9lhqV9NhAp0uHAwEADUFFBMUZAUdJgQVPlNnhREvSI3MDQw0IsPuSikA0GjFU2CTPwNMjHVjWJmRQVkLStei7RY2N2NiSVpVlD4fTTK1uxndVNTLVSUOvaTZNiLsKyzU9pIvSTUI89MvIXZAG2k09YlbdtSk8zNezQftTHMKxbDMFBQ0ndhLGYGw-ACIJQqXKJqBLRIZDBYEBM3bcuF3ewgszKsSrPcI-LUgLpkqCAaH061YvQA42pc-NSPCdzOu6lBermfq0GMjFTJYYEAFkUg4wTZrqfcYAAMRYFJlps3xnOpVC3PIv8uAsWMmpiXzE3a69Yium7FzAcKV0ittMD7AdkuHaYNAnNwYAAcTtTFcrnAqQmYQYIsSUHKs3dg7WKWbbsoc8HuGyZkB4cHqi4abZsGnHTuvDTQMZUE3EJ1QSYagbo00hbwlFJGoVRomEmyPaDqOuyTqvEb0Mwi7GW51RecxqB7pzR7Rf-CGZbej7mq+6KSilrhefixLBxSmxsAsKBsAQAw4DNAx6dnfKQth-4EeSNIslyKX0aZtAOyliU7RPFdsYV3GiR01l6Zmr2al9ozhZIlAg4-EObzDwII5j6oyeDinRap1j-yeemuEj2yvPmsD2cBTmYB13ndv2w7ZpgOOpPF1m-1rzIW9QxPkJzyZU5QCUUG8WmeDmTuajQEf6azpP+9G9z9DAYfvHpsewAnlXMnLtRTPMj0ue35ufPjIaF5vKXeIdsL4wiqKfbtXj9b+odbEcFAyQgbwYAAKQgB8YM7S2G0AgUAQYb5FWdkkZ4bscge3qqXTMHY3pwAgHeKAdQr4yADs1XuxEDSxAAFYALQBHTa2CaioPQdAOefcRbfjGhLOSuEFLhyfjILe1ReK7zYpXWI1da7YK7ufBhKhzrt0lirYR3dKatXJmIlONCoBimJjhTowBQEoDmNQjB0dpEcJ3rI3OTDJH3CsFoNOdouHS2ETUUmLNqZ8NFAfT0QjDGy3lvPRRz1roYkge9O+n0oo-QSq-I2ZhgDOEQLpWAwBsDm0IJ+O2843pO0+mVCqVVchGFlvg-yT0NCfyeCgGQ2g5h0IIT3UxTjYhFItoEMpFTHGsUWitNagluKgiOsY0QEjakvX8afUIoj46+NeoVQJoR77fV+kAA
