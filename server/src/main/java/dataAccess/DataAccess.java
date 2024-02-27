package dataAccess;

import model.*;

import java.util.Collection;

public interface DataAccess {
    AuthData addAuth(AuthData auth) throws DataAccessException;

    Collection<AuthData> listAuth() throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    void deleteAllAuths() throws DataAccessException;


    GameData addGame(GameData game) throws DataAccessException;

    Collection<GameData> listGame() throws DataAccessException;

    GameData getGame(Integer gameID) throws DataAccessException;

    void deleteGame(Integer gameID) throws DataAccessException;

    void deleteAllGames() throws DataAccessException;


    UserData addUser(UserData user) throws DataAccessException;

    Collection<UserData> listUser() throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void deleteUser(String username) throws DataAccessException;

    void deleteAllUser() throws DataAccessException;
}
