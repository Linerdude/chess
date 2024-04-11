package webSocketMessages.serverMessages;

import model.GameData;

public class LoadGame extends ServerMessage{
    public GameData game;
    public LoadGame(ServerMessageType type, GameData game) {
        super(type);
        this.game = game;
    }
}
