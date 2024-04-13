package ConsoleClient;

import exception.ResponseException;

public class ClientRun {

    public static void main(String[] args) throws ResponseException {
        String serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        new Console(8080).run();
    }
}
