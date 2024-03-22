package ConsoleClient;

import server.Server;
import service.GameService;
import dataAccess.DataAccessException;

class ConsoleServerRun {
    private static final Server server = new Server();
    private static final GameService gameService;

    static {
        try {
            gameService = new GameService();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Console client = new Console();

    private static void setUp() { server.run(8080); }

    public static void closeServer() { server.stop(); }

    public static void main(String [] args) throws DataAccessException {
        setUp();
        clearDatabase();
        client.run();
        clearDatabase();
        closeServer();
    }

    public static void clearDatabase() throws DataAccessException { gameService.clearApplication(); }
}