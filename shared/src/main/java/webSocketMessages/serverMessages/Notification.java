package webSocketMessages.serverMessages;

public class Notification extends ServerMessage {
    public String message;
    public Notification(ServerMessageType type, String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }
}
