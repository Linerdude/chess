package dataAccess;

import model.*;

import java.util.Collection;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {
    final private static HashMap<String, UserData> users = new HashMap<>();

    public UserData addUser(UserData user) {
        user = new UserData(user.username(), user.password(), user.email());
        users.put(user.username(), user);
        return user;
    }

    public UserData getUser(String username) {
        return users.get(username);
    }

    public void deleteAllUser() {
        users.clear();
    }


    final private static HashMap<Integer, GameData> games = new HashMap<>();

    public GameData addGame(GameData game) {
        game = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(),game.gameName(), game.game());

        games.put(game.gameID(), game);
        return game;
    }

    public Collection<GameData> listGame() {
        return games.values();
    }

    public GameData getGame(Integer gameID) {
        return games.get(gameID);
    }

    public void updateGame(Integer gameID, GameData game){
        games.put(gameID,game);
    }

    public void deleteAllGames() {
        games.clear();
    }




    final private static HashMap<String, AuthData> auths = new HashMap<>();

    public AuthData addAuth(AuthData auth) {
        auth = new AuthData(auth.authToken(), auth.username());

        auths.put(auth.authToken(), auth);
        return auth;
    }

    public Collection<AuthData> listAuth() {
        return auths.values();
    }

    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }

    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }

    public void deleteAllAuths() {
        auths.clear();
    }



}
