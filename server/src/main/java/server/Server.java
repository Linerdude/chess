package server;

import handlers.AuthorizationHandler;
import handlers.GameHandler;
import spark.*;
import webSocket.WebSocketHandler;

public class Server {

    WebSocketHandler webSocketHandler;

    public Server (){
        WebSocketHandler webSocketHandler = new WebSocketHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        //
        Spark.webSocket("/connect", webSocketHandler);
        Spark.post("/user", (req, res) -> new AuthorizationHandler(req, res).register());
        Spark.post("/session", (req, res) -> new AuthorizationHandler(req, res).login());
        Spark.delete("/session", (req, res) -> new AuthorizationHandler(req, res).logout());
        Spark.get("/game", (req, res) -> new GameHandler(req, res).listGames());
        Spark.post("/game", (req, res) -> new GameHandler(req, res).createGame());
        Spark.put("/game", (req, res) -> new GameHandler(req, res).joinGame());
        Spark.delete("/db", (req, res) -> new GameHandler(req, res).clear());

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
