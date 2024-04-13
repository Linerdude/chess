package webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final HashMap<String, Connection> connections = new HashMap<>();

    public void add(String authToken, Session session) {
        var connection = new Connection(authToken, session);
        connections.put(authToken, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void broadcast(String excludeVisitorName, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(excludeVisitorName)) {
                    c.send(notification);
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.authToken);
        }
    }

    public void clientNotify(String authToken, ServerMessage notification) throws IOException {
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.authToken.equals(authToken)) {
                    String not = new Gson().toJson(notification);
                    System.out.println(not);
                    c.send(notification);
                    return;
                }
            }
        }
    }
}
