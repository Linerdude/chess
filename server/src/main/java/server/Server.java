package server;

import dataAccess.DataAccessException;
import dataAccess.SQLDataAccess;
import handlers.AuthorizationHandler;
import handlers.GameHandler;
import spark.*;
import webSocket.WebSocketHandler;

public class Server {

    static SQLDataAccess authDAO;
    static SQLDataAccess gameDAO;
    static SQLDataAccess userDAO;
    static WebSocketHandler webSocketHandler;

    public Server (){
        try {
            authDAO = new SQLDataAccess();
            gameDAO = new SQLDataAccess();
            userDAO = new SQLDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        webSocketHandler = new WebSocketHandler(authDAO,gameDAO,userDAO);
    }

    public static void main(String [] args){
        try {
            authDAO = new SQLDataAccess();
            gameDAO = new SQLDataAccess();
            userDAO = new SQLDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        webSocketHandler = new WebSocketHandler(authDAO,gameDAO,userDAO);
        Server.run(8080);
    }

    public static int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        //
        Spark.webSocket("/connect", webSocketHandler);
        Spark.init();
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
