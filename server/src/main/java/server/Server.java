package server;

import dataAccess.DataAccessException;
import dataAccess.SQLDataAccess;
import handlers.AuthorizationHandler;
import handlers.GameHandler;
import spark.*;
import webSocket.WebSocketHandler;

public class Server {

    SQLDataAccess authDAO;
    SQLDataAccess gameDAO;
    SQLDataAccess userDAO;
    WebSocketHandler webSocketHandler;

    public Server (){
        try {
            SQLDataAccess authDAO = new SQLDataAccess();
            SQLDataAccess gameDAO = new SQLDataAccess();
            SQLDataAccess userDAO = new SQLDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        WebSocketHandler webSocketHandler = new WebSocketHandler(authDAO,gameDAO,userDAO);
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
