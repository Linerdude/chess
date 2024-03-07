package handlers;

import com.google.gson.Gson;
import spark.*;
import service.UserService;
import requestRecords.RegisterRequest;
import requestRecords.LoginRequest;
import responseRecords.LoginResponse;
import responseRecords.RegisterResponse;
import responseRecords.ServerResponse;
import dataAccess.DataAccessException;

public class AuthorizationHandler extends ServiceHandler {
    private final UserService service = new UserService();

    public AuthorizationHandler(Request request, Response response) throws DataAccessException { super(request, response); }

    public Object register() {
        String body;
        RegisterRequest rRequest = getBody(this.request, RegisterRequest.class);
        try {
            RegisterResponse rResponse = this.service.register(rRequest);
            body = new Gson().toJson(rResponse);
            this.response.status(200);
        } catch (DataAccessException ex) {
            body = new Gson().toJson(new ServerResponse(ex.getMessage()));
            switch (ex.getMessage()) {
                case "Error: bad request" -> this.response.status(400);
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

    public Object login() {
        String body;
        LoginRequest lRequest = getBody(this.request, LoginRequest.class);
        try {
            LoginResponse lResponse = this.service.login(lRequest);
            body = new Gson().toJson(lResponse);
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

    public Object logout() {
        String body;
        String authToken = this.request.headers("authorization");
        try {
            this.service.logout(authToken);
            body = new Gson().toJson(new ServerResponse(""));
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
}