package handlers;

import com.google.gson.Gson;
import spark.*;
import service.GameService;
import requestRecords.CreateGameRequest;
import requestRecords.JoinGameRequest;
import responseRecords.ListGamesResponse;
import responseRecords.CreateGameResponse;
import responseRecords.ServerResponse;
import dataAccess.DataAccessException;

public class GameHandler extends ServiceHandler {
    private final GameService service = new GameService();

    public GameHandler(Request request, Response response) throws DataAccessException { super(request, response); }

    public Object listGames() {
        String body;
        String authToken = this.request.headers("authorization");
        try {
            ListGamesResponse lgResponse = this.service.listGames(authToken);
            body = new Gson().toJson(lgResponse);
            this.response.status(200);
        } catch (DataAccessException ex) {
            body = new Gson().toJson(new ServerResponse(ex.getMessage()));
            this.response.status(401);
        } catch (Exception ex) {
            body = new Gson().toJson(new ServerResponse(ex.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return body;
    }

    public Object createGame() {
        String body;
        String authToken = this.request.headers("authorization");
        CreateGameRequest cgRequest = getBody(this.request, CreateGameRequest.class);
        try {
            CreateGameResponse cgResponse = this.service.createGame(cgRequest, authToken);
            body = new Gson().toJson(cgResponse);
            this.response.status(200);
        } catch (DataAccessException ex) {
            body = new Gson().toJson(new ServerResponse(ex.getMessage()));
            this.response.status(401);
        } catch (Exception ex) {
            body = new Gson().toJson(new ServerResponse(ex.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return body;
    }

    public Object joinGame() {
        String body;
        String authToken = this.request.headers("authorization");
        JoinGameRequest jgRequest = getBody(this.request, JoinGameRequest.class);
        try {
            this.service.joinGame(jgRequest, authToken);
            body = new Gson().toJson(new ServerResponse(""));
            this.response.status(200);
        } catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
            body = new Gson().toJson(new ServerResponse(ex.getMessage()));
            switch (ex.getMessage()) {
                case "Error: bad request" -> this.response.status(400);
                case "Error: unauthorized" -> this.response.status(401);
                case "Error: already taken" -> this.response.status(403);
                default -> this.response.status(418);
            }
        } catch (Exception ex) {
            body = new Gson().toJson(new ServerResponse(ex.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return body;
    }



    public Object clear() {
        String body;
        try {
            this.service.clearApplication();
            body = new Gson().toJson(new ServerResponse(""));
            this.response.status(200);
        } catch (Exception ex) {
            body = new Gson().toJson(new ServerResponse(ex.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return body;
    }
}