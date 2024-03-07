package dataAccessTests;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;
import service.UserService;
import service.GameService;
import requestRecords.RegisterRequest;
import requestRecords.CreateGameRequest;
import requestRecords.JoinGameRequest;
import dataAccess.DataAccessException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameServiceTest {
    private final UserService authService = new UserService();

    private final GameService doService = new GameService();

    private final GameService gameService = new GameService();

    private String ryanAuth;

    GameServiceTest() throws DataAccessException {
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
    @DisplayName("createGame (+)")
    void createGame_positive() throws DataAccessException {
        this.gameService.createGame(new CreateGameRequest("chest.org"), ryanAuth);
        int length = this.gameService.listGames(this.ryanAuth).games().size();
        assertEquals(4, length);
    }

    @Test
    @Order(4)
    @DisplayName("createGame (-)")
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
    @DisplayName("clear (+)")
    void clear_positive() throws DataAccessException {
        this.doService.clearApplication();
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.authService.logout(this.ryanAuth));
        assertEquals("Error: unauthorized", exception.getMessage());
    }
}