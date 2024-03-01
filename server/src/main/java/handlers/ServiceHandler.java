package handlers;

import com.google.gson.Gson;
import spark.*;

public abstract class ServiceHandler {
    protected Request request;
    protected Response response;

    public ServiceHandler(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    protected static <T> T getBody(Request request, Class<T> clazz) {
        var body = new Gson().fromJson(request.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }
}