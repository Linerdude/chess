package ServerFacade;

import com.google.gson.Gson;
import exception.ResponseException;
import requestRecords.CreateGameRequest;
import requestRecords.JoinGameRequest;
import requestRecords.LoginRequest;
import requestRecords.RegisterRequest;
import responseRecords.CreateGameResponse;
import responseRecords.ListGamesResponse;
import responseRecords.LoginResponse;
import responseRecords.RegisterResponse;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public RegisterResponse register(RegisterRequest request) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, request, RegisterResponse.class,null);
    }

    public LoginResponse login(LoginRequest request) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, request, LoginResponse.class, null);
    }

    public void logout(String authToken) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public ListGamesResponse listGames(String authToken) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListGamesResponse.class,authToken);
    }

    public CreateGameResponse createGame(CreateGameRequest request, String authToken) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, request, CreateGameResponse.class, authToken);
    }

    public void joinGame(JoinGameRequest request, String authToken) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, request, null, authToken);
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }




    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authHeader) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if (!(authHeader == null)) { http.setRequestProperty("authorization", authHeader); }
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
