package service;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import dataAccess.MemoryDataAccess;

import requestRecords.CreateGameRequest;
import requestRecords.JoinGameRequest;
import responseRecords.ListGamesResponse;
import responseRecords.ListGameInfo;
import responseRecords.CreateGameResponse;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class GameService {
    private final MemoryDataAccess dataAccess;

    public GameService() {
        this.dataAccess = new MemoryDataAccess();
    }

    public ListGamesResponse listGames(String authToken) throws DataAccessException {
        ArrayList<AuthData> authList = (ArrayList<AuthData>) dataAccess.listAuth();
        ArrayList<String> authTokens = null;
        for (AuthData curAuth : authList){
            authTokens.add(curAuth.username());
        }

        if (validateAuthToken(authToken)) {
            AuthData auth = dataAccess.getAuth(authToken);
            String cur_username = auth.username();
            ArrayList<GameData> cur_games = (ArrayList<GameData>) dataAccess.listGame();
            ArrayList<ListGameInfo> return_games = new ArrayList<>();

            for (GameData game : cur_games) {
                if (Objects.equals(game.blackUsername(), cur_username) || Objects.equals(game.whiteUsername(), cur_username)) {

                    return_games.add(new ListGameInfo(game.gameID(),game.whiteUsername(), game.blackUsername(), game.gameName()));
                }
            }
            return new ListGamesResponse(return_games);
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public CreateGameResponse createGame(CreateGameRequest cgRequest, String authToken) throws DataAccessException{
        Collection<GameData> cur_games =  dataAccess.listGame();
        ArrayList<String> cur_names = new ArrayList<>();
        if (validateAuthToken(authToken)) {
            int cur_ID = 0;
            for (GameData game : cur_games) {
                cur_ID = game.gameID();
                cur_names.add(game.gameName());
            }
            if (!cur_names.contains(cgRequest.gameName())) {
                int new_id = cur_ID + 1;
                GameData new_game = new GameData(new_id, null, null, cgRequest.gameName(), new ChessGame());
                dataAccess.addGame(new_game);
                return new CreateGameResponse(new_id);
            }
            else {
                return null;
            }

        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void joinGame(JoinGameRequest jgRequest, String authToken) throws DataAccessException {
        if (validateAuthToken(authToken)) {
            AuthData auth = dataAccess.getAuth(authToken);
            String cur_username = auth.username();
            ArrayList<GameData> games = (ArrayList<GameData>) dataAccess.listGame();
            Integer gameID = jgRequest.gameID();
//        Do I need to add gameID checker? or is it based off the exception thrown
            GameData cur_game = dataAccess.getGame(gameID);
            if (cur_game == null){
                throw new DataAccessException("Error: bad request");
            }

            String new_blk_username = cur_game.blackUsername();
            String new_wht_username = cur_game.whiteUsername();
            if (jgRequest.playerColor() == ChessGame.TeamColor.BLACK) {
                if (new_blk_username == null) {
                    new_blk_username = cur_username;
                } else {
                    throw new DataAccessException("Error: bad request");
                }
            } else if (jgRequest.playerColor() == ChessGame.TeamColor.WHITE) {
                if (new_wht_username == null) {
                    new_wht_username = cur_username;
                }
                else {
                    throw new DataAccessException("Error: bad request");
                }
            }
            GameData new_game = new GameData(gameID, new_wht_username, new_blk_username, cur_game.gameName(), cur_game.game());
            dataAccess.updateGame(gameID, new_game);
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void clearApplication(){
        dataAccess.deleteAllAuths();
        dataAccess.deleteAllUser();
        dataAccess.deleteAllGames();
    }

    public boolean validateAuthToken(String authToken){
        ArrayList<AuthData> authList = (ArrayList<AuthData>) dataAccess.listAuth();
        ArrayList<String> authTokens = null;
        for (AuthData curAuth : authList){
            authTokens.add(curAuth.username());
        }
        return authTokens.contains(authToken);
    }

}
