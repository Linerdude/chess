package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;
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


            conn.setCatalog(DatabaseManager.getDatabaseName());

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
                    game longtext NOT NULL,
                    PRIMARY KEY (gameID)
            )""";


            var createAuthTable = """
                CREATE TABLE  IF NOT EXISTS authTable (
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

    public AuthData addAuth(AuthData auth) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO authTable (authToken,username) VALUES(?, ?)")) {
                preparedStatement.setString(1, auth.authToken());
                preparedStatement.setString(2, auth.username());


                preparedStatement.executeUpdate();


                return auth;
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
    }

    public Collection<AuthData> listAuth() throws DataAccessException {
        ArrayList<AuthData> returnList = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT authToken, username FROM authTable")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var authToken = rs.getString("authToken");
                        var username = rs.getString("username");

                        returnList.add(new AuthData(authToken,username));
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
        return returnList;
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username FROM authTable WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var username = rs.getString("username");

                        return new AuthData(authToken,username);
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM authTable WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
    }

    public void deleteAllAuths() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE authTable")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
    }

    @Override
    public GameData addGame(GameData game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO gameTable (gameID,whiteUsername,blackUsername,gameName,game) VALUES(?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, game.gameID());
                preparedStatement.setString(2, game.whiteUsername());
                preparedStatement.setString(3, game.blackUsername());
                preparedStatement.setString(4, game.gameName());

                var json = new Gson().toJson(game.game());
                preparedStatement.setString(5, json); // Add serialization

                preparedStatement.executeUpdate();


                return game;
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
    }

    public Collection<GameData> listGame() throws DataAccessException {
        ArrayList<GameData> returnList = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameTable")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var gameID = rs.getInt("gameID");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");

                        var json = rs.getString("game"); // Add deserialization
                        var game = new Gson().fromJson(json, ChessGame.class);

                        returnList.add(new GameData(gameID,whiteUsername,blackUsername,gameName,game));
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
        return returnList;
    }

    public GameData getGame(Integer gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT whiteUsername, blackUsername, gameName, game FROM gameTable WHERE gameID=?")) {
                preparedStatement.setInt(1, gameID);
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");

                        var json = rs.getString("game"); // Add deserialization
                        var game = new Gson().fromJson(json, ChessGame.class);

                        return new GameData(gameID,whiteUsername,blackUsername,gameName,game);
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
        return null;
    }

    public void updateGame(Integer gameID, GameData game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("UPDATE gameTable SET whiteUsername=?, blackUsername=?,gameName=?,game=? WHERE gameID=?")) {
                preparedStatement.setString(1, game.whiteUsername());
                preparedStatement.setString(2, game.blackUsername());
                preparedStatement.setString(3, game.gameName());

                var json = new Gson().toJson(game.game());
                preparedStatement.setString(4, json);

                preparedStatement.setInt(5,gameID);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
    }

    public void deleteAllGames() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE gameTable")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.toString());
        }
    }

        @Override
    public UserData addUser(UserData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO userTable (username, password, email) VALUES(?, ?, ?)")) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String hashedPassword = encoder.encode(user.password());

//                TODO: fix hashed password, esp in getUser below
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, user.email());

                preparedStatement.executeUpdate();


                return user;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.toString());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT password, email FROM userTable WHERE username=?")) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var password = rs.getString("password");
                        var email = rs.getString("email");

                        return new UserData(username,password,email);
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
        return null;
    }

    @Override
    public void deleteAllUser() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE userTable")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
    }

}
