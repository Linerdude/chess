package dataAccessTests;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

import requestRecords.LoginRequest;
import service.UserService;
import service.GameService;
import requestRecords.RegisterRequest;
import requestRecords.CreateGameRequest;
import requestRecords.JoinGameRequest;
import dataAccess.DataAccessException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DataBaseUnitTests {
    private final UserService authService = new UserService();

    private final GameService doService = new GameService();

    private final GameService gameService = new GameService();

    private String ryanAuth;


    DataBaseUnitTests() throws DataAccessException {
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        String testAuth = this.authService.register(new RegisterRequest(
                "testUsername", "testPassword", "testEmail")).authToken();
        String tammyAuth = this.authService.register(new RegisterRequest(
                "tammyUsername", "tammyPassword", "3@gmail.com")).authToken();
        this.ryanAuth = this.authService.register(new RegisterRequest(
                "5john2", "thisisapen", "ryan@bop.com")).authToken();
        String rebeccaAuth = this.authService.register(new RegisterRequest(
                "becca23", "yoyoyogas", "bananabread@star.com")).authToken();
        this.authService.logout(rebeccaAuth);
        this.gameService.createGame(new CreateGameRequest("penpineappleapplepen"), this.ryanAuth);
        this.gameService.createGame(new CreateGameRequest("hiccup"), tammyAuth);
        this.gameService.createGame(new CreateGameRequest("tylerisaboss"), testAuth);
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        this.doService.clearApplication();
    }

    @Test
    @Order(1)
    @DisplayName("listGames (+)")
    void listGames_positive() throws DataAccessException {
        int length = this.gameService.listGames(this.ryanAuth).games().size();
        assertEquals(4, length);
    }

    @Test
    @Order(2)
    @DisplayName("listGames (-)")
    void listGames_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.gameService.listGames(UUID.randomUUID().toString()));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("addGame (+)")
    void createGame_positive() throws DataAccessException {
        this.gameService.createGame(new CreateGameRequest("chest.org"), ryanAuth);
        int length = this.gameService.listGames(this.ryanAuth).games().size();
        assertEquals(4, length);
    }

    @Test
    @Order(4)
    @DisplayName("addGame (-)")
    void createGame_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                this.gameService.createGame(new CreateGameRequest("chest.org"), UUID.randomUUID().toString()));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("joinGame (+)")
    void joinGame_positive() throws DataAccessException {
        int newGameID = this.gameService.createGame(new CreateGameRequest("chest.org"), ryanAuth).gameID();
        this.gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, newGameID), ryanAuth);
    }

    @Test
    @Order(6)
    @DisplayName("joinGame (-)")
    void joinGame_negative() throws DataAccessException {
        int newGameID = this.gameService.createGame(new CreateGameRequest("chest.org"), ryanAuth).gameID();
        this.gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, newGameID), ryanAuth);
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                this.gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, newGameID), ryanAuth));
        assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    @Order(7)
    @DisplayName("clearAuth (+)")
    void clear_positive() throws DataAccessException {
        this.doService.clearApplication();
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.authService.logout(this.ryanAuth));
        assertEquals("Error: unauthorized", exception.getMessage());
    }


    @Test
    @Order(8)
    @DisplayName("addaAuth (+)")
    void register_positive() throws DataAccessException {
        String tylerAuth = this.authService.register(new RegisterRequest(
                "tyleransom", "applepie1", "appletyler@pie.com")).authToken();
        this.authService.logout(tylerAuth);
    }

    @Test
    @Order(9)
    @DisplayName("addAuth (-)")
    void register_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.authService.register(new RegisterRequest("tyleransom", null, "appletyler@pie.com")));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    @Order(10)
    @DisplayName("getAuth (+)")
    void login_positive() throws DataAccessException {
        String newAuth = this.authService.login(new LoginRequest("becca23", "yoyoyogas")).authToken();
        this.authService.logout(newAuth);
    }

    @Test
    @Order(11)
    @DisplayName("getAuth (-)")
    void login_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.authService.login(new LoginRequest("becca", "yoyoyogas")));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(12)
    @DisplayName("deleteAuth (+)")
    void logout_positive() throws DataAccessException {
        this.authService.logout(this.ryanAuth);
    }

    @Test
    @Order(13)
    @DisplayName("deleteAuth (-)")
    void logout_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.authService.logout(UUID.randomUUID().toString()));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Order(14)
    @DisplayName("getGames (+)")
    void getGames_positive() throws DataAccessException {
        int length = this.gameService.listGames(this.ryanAuth).games().size();
        assertEquals(4, length);
    }

    @Test
    @Order(15)
    @DisplayName("getGames (-)")
    void getGames_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.gameService.listGames(UUID.randomUUID().toString()));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(16)
    @DisplayName("updateGame (+)")
    void updateGame_positive() throws DataAccessException {
        this.gameService.createGame(new CreateGameRequest("chest.org"), ryanAuth);
        int length = this.gameService.listGames(this.ryanAuth).games().size();
        assertEquals(4, length);
    }

    @Test
    @Order(17)
    @DisplayName("updateGame (-)")
    void updateGame_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                this.gameService.createGame(new CreateGameRequest("chest.org"), UUID.randomUUID().toString()));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

//    @Test
//    @Order(18)
//    @DisplayName("joinGame (+)")
//    void joinGame_positive() throws DataAccessException {
//        int newGameID = this.gameService.createGame(new CreateGameRequest("chest.org"), ryanAuth).gameID();
//        this.gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, newGameID), ryanAuth);
//    }
//
//    @Test
//    @Order(19)
//    @DisplayName("joinGame (-)")
//    void joinGame_negative() throws DataAccessException {
//        int newGameID = this.gameService.createGame(new CreateGameRequest("chest.org"), ryanAuth).gameID();
//        this.gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, newGameID), ryanAuth);
//        DataAccessException exception = assertThrows(DataAccessException.class, () ->
//                this.gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, newGameID), ryanAuth));
//        assertEquals("Error: already taken", exception.getMessage());
//    }

    @Test
    @Order(19)
    @DisplayName("clearUser (+)")
    void clearUser_positive() throws DataAccessException {
        this.doService.clearApplication();
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.authService.logout(this.ryanAuth));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(20)
    @DisplayName("clearGames (+)")
    void clearGames_positive() throws DataAccessException {
        this.doService.clearApplication();
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.authService.logout(this.ryanAuth));
        assertEquals("Error: unauthorized", exception.getMessage());
    }


    @Test
    @Order(21)
    @DisplayName("listAuth (+)")
    void listAuth_positive() throws DataAccessException {
        String tylerAuth = this.authService.register(new RegisterRequest(
                "tyleransom", "applepie1", "appletyler@pie.com")).authToken();
        this.authService.logout(tylerAuth);
    }

    @Test
    @Order(22)
    @DisplayName("listAuth (-)")
    void listAuth_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.authService.register(new RegisterRequest("tyleransom", null, "appletyler@pie.com")));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    @Order(23)
    @DisplayName("addUser (+)")
    void addUser_positive() throws DataAccessException {
        String newAuth = this.authService.login(new LoginRequest("becca23", "yoyoyogas")).authToken();
        this.authService.logout(newAuth);
    }

    @Test
    @Order(24)
    @DisplayName("addUser (-)")
    void addUser_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.authService.login(new LoginRequest("becca", "yoyoyogas")));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(25)
    @DisplayName("getUser (+)")
    void getUser_positive() throws DataAccessException {
        this.authService.logout(this.ryanAuth);
    }

    @Test
    @Order(26)
    @DisplayName("getUser (-)")
    void getUser_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.authService.logout(UUID.randomUUID().toString()));
        assertEquals("Error: unauthorized", exception.getMessage());
    }
}
