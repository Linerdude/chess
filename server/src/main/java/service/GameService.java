package service;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import dataAccess.MemoryDataAccess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class GameService {
    private final MemoryDataAccess dataAccess;

    public GameService() {
        this.dataAccess = new MemoryDataAccess();
    }

    public Collection<GameData> listGames(AuthData auth){
        String cur_username = auth.username();
        Collection<GameData> cur_games =  dataAccess.listGame();
        Collection<GameData> return_games = new ArrayList<>();

        for (GameData game : cur_games){
            if (Objects.equals(game.blackUsername(), cur_username) || Objects.equals(game.whiteUsername(), cur_username)){
                return_games.add(game);
            }
        }
        return return_games;
    }
    public GameData createGame(String gameName){
        Collection<GameData> cur_games =  dataAccess.listGame();
        ArrayList<String> cur_names = new ArrayList<>();
        int cur_ID = 0;
        for (GameData game : cur_games){
            cur_ID = game.gameID();
            cur_names.add(game.gameName());
        }
        if (!cur_names.contains(gameName)){
            int new_id = cur_ID + 1;
            GameData new_game = new GameData(new_id, null,null,gameName,new ChessGame());
            dataAccess.addGame(new_game);
            return new_game;
        }
        return null;
    }

    public void joinGame(AuthData auth){

    }

    public void clearApplication(){

    }

}
