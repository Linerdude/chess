package webSocketMessages.serverMessages;

public class Notification extends ServerMessage {
    public String message;
    public Notification(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }
}
