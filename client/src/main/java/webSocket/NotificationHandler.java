package webSocket;

import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

public interface NotificationHandler {
    void notify(UserGameCommand notification);
}