package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.Collection;

public class SQLDataAccess implements DataAccess {

    public SQLDataAccess() throws DataAccessException {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        CreateTables();

    }

    public void CreateTables() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {

            conn.setCatalog("chess");

            var createUserTable = """
                CREATE TABLE  IF NOT EXISTS userTable (
                    username VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    PRIMARY KEY (username)
            )""";

            var createGameTable = """
                CREATE TABLE  IF NOT EXISTS gameTable (
                    gameID INT NOT NULL,
                    whiteUsername VARCHAR(255),
                    blackUsername VARCHAR(255),
                    gameName VARCHAR(255) NOT NULL,
                    game VARCHAR(255) NOT NULL,
                    PRIMARY KEY (gameID)
            )""";


            var createAuthTable = """
                CREATE TABLE  IF NOT EXISTS pet (
                    authToken VARCHAR(255) NOT NULL,
                    username VARCHAR(255) NOT NULL,
                    PRIMARY KEY (authToken)
            )""";


            try (var createTableStatement = conn.prepareStatement(createUserTable)) {
                createTableStatement.executeUpdate();
            }
            try (var createTableStatement = conn.prepareStatement(createGameTable)) {
                createTableStatement.executeUpdate();
            }
            try (var createTableStatement = conn.prepareStatement(createAuthTable)) {
                createTableStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.toString());
        }
    }

    @Override
    public AuthData addAuth(AuthData auth) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<AuthData> listAuth() throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void deleteAllAuths() throws DataAccessException {

    }

    @Override
    public GameData addGame(GameData game) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGame() throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(Integer gameID, GameData game) throws DataAccessException {

    }

    @Override
    public void deleteAllGames() throws DataAccessException {

    }

    @Override
    public UserData addUser(UserData user) throws DataAccessException {
        return null;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAllUser() throws DataAccessException {

    }
}
