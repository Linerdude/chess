package clientTests;

import ServerFacade.ServerFacade;
import chess.ChessGame;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import requestRecords.CreateGameRequest;
import requestRecords.JoinGameRequest;
import requestRecords.LoginRequest;
import requestRecords.RegisterRequest;
import responseRecords.ListGameInfo;
import server.Server;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:"+port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void startClear() throws ResponseException { facade.clear(); }

    @AfterEach
    public void endClear() throws ResponseException { facade.clear(); }


    @Test
    @Order(1)
    @DisplayName("Register (+)")
    public void registerPos() throws ResponseException {
        String testAuth = facade.register(new RegisterRequest("TestUser", "TestPassword","TestEmail")).authToken();
        facade.logout(testAuth);
    }

    @Test
    @Order(2)
    @DisplayName("Register (-)")
    public void registerNeg() {
        ResponseException exception = assertThrows(ResponseException.class, () ->
                facade.register(new RegisterRequest("", null, "")));
        assertEquals("failure: 400", exception.getMessage());
    }


    @Test
    @Order(3)
    @DisplayName("Login (+)")
    public void loginPos() throws ResponseException {
        String testAuth = facade.register(new RegisterRequest("TestUser", "TestPassword","TestEmail")).authToken();
        facade.logout(testAuth);
        LoginRequest testLogin = new LoginRequest("TestUser", "TestPassword");
        assertInstanceOf(String.class, facade.login(testLogin).authToken());
    }

    @Test
    @Order(4)
    @DisplayName("Login (-)")
    public void loginNeg() {
        LoginRequest testLogin = new LoginRequest("TestUser", "TestPassword");
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.login(testLogin));
        assertEquals("failure: 401", exception.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("Logout (+)")
    public void logoutPos() throws ResponseException {
        String testAuth = facade.register(new RegisterRequest("TestUser", "TestPassword","TestEmail")).authToken();
        facade.logout(testAuth);
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.logout(testAuth));
        assertEquals("failure: 401", exception.getMessage());
    }

    @Test
    @Order(6)
    @DisplayName("Logout (-)")
    public void logoutNeg() {
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.logout("FailureTest"));
        assertEquals("failure: 401", exception.getMessage());
    }

    @Test
    @Order(7)
    @DisplayName("CreateGame (+)")
    public void createGamesPos() throws ResponseException {
        String testAuth = facade.register(new RegisterRequest("TestUser", "TestPassword","TestEmail")).authToken();
        int gameID = facade.createGame(new CreateGameRequest("testGame1"),testAuth).gameID();
        assertEquals(facade.listGames(testAuth).games().getFirst().gameID(), gameID);
    }

    @Test
    @Order(8)
    @DisplayName("CreateGame (-)")
    public void createGamesNeg() throws ResponseException {
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.createGame(new CreateGameRequest("FailureTest"), "FailureTest"));
        assertEquals("failure: 401", exception.getMessage());
    }

    @Test
    @Order(9)
    @DisplayName("ListGames (+)")
    public void listGamesPos() throws ResponseException {
        String testAuth = facade.register(new RegisterRequest("TestUser", "TestPassword","TestEmail")).authToken();
        facade.createGame(new CreateGameRequest("testGame1"),testAuth);
        facade.createGame(new CreateGameRequest("testGame2"),testAuth);
        facade.createGame(new CreateGameRequest("testGame3"),testAuth);
        facade.createGame(new CreateGameRequest("testGame4"),testAuth);
        facade.createGame(new CreateGameRequest("testGame5"),testAuth);
        ArrayList<ListGameInfo> games = facade.listGames(testAuth).games();
        assertEquals(new ListGameInfo(3,null,null,"testGame3"), games.get(2));
    }

    @Test
    @Order(10)
    @DisplayName("ListGames (-)")
    public void listGamesNeg() {
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.listGames("FailureTest"));
        assertEquals("failure: 401", exception.getMessage());
    }


    @Test
    @Order(11)
    @DisplayName("JoinGame (+)")
    public void joinGamesPos() throws ResponseException {
        String testAuth1 = facade.register(new RegisterRequest("TestUser1", "TestPassword","TestEmail")).authToken();
        String testAuth2 = facade.register(new RegisterRequest("TestUser2", "TestPassword","TestEmail")).authToken();
        int gameID = facade.createGame(new CreateGameRequest("TestGame"), testAuth1).gameID();
        facade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID),testAuth1);
        facade.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID),testAuth2);
        ListGameInfo gameInfo = facade.listGames(testAuth1).games().getFirst();
        assertEquals(new ListGameInfo(1, "TestUser1", "TestUser2","TestGame"), gameInfo);
    }

    @Test
    @Order(12)
    @DisplayName("JoinGame (-)")
    public void joinGamesNeg() throws ResponseException {
        String testAuth1 = facade.register(new RegisterRequest("TestUser1", "TestPassword","TestEmail")).authToken();
        int gameID = facade.createGame(new CreateGameRequest("TestGame"), testAuth1).gameID();
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 3),testAuth1));
        assertEquals("failure: 400", exception.getMessage());
    }

    @Test
    @Order(13)
    @DisplayName("Clear (+)")
    public void clearPos() throws ResponseException {
        String testAuth1 = facade.register(new RegisterRequest("TestUser1", "TestPassword","TestEmail")).authToken();
        facade.createGame(new CreateGameRequest("TestGame"),testAuth1);
        facade.clear();
        testAuth1 = facade.register(new RegisterRequest("TestUser1", "TestPassword","TestEmail")).authToken();
        assertEquals(0, facade.listGames(testAuth1).games().size());
    }

}
