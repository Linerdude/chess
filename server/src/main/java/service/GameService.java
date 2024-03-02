package service;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import dataAccess.DataAccessException;
import handlers.ServiceHandler;
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
    private final MemoryDataAccess dataAccess = new MemoryDataAccess();


    public ListGamesResponse listGames(String authToken) throws DataAccessException {

        if (validateAuthToken(authToken)) {
            AuthData auth = dataAccess.getAuth(authToken);
            ArrayList<GameData> curGames = new ArrayList<>(dataAccess.listGame());
            ArrayList<ListGameInfo> returnGames = new ArrayList<>();

            for (GameData game : curGames) {
                    returnGames.add(new ListGameInfo(game.gameID(),game.whiteUsername(), game.blackUsername(), game.gameName()));
                }
            return new ListGamesResponse(returnGames);
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public CreateGameResponse createGame(CreateGameRequest cgRequest, String authToken) throws DataAccessException{
        Collection<GameData> curGames =  dataAccess.listGame();
        ArrayList<String> curNames = new ArrayList<>();
        if (validateAuthToken(authToken)) {
            int curID = 0;
            for (GameData game : curGames) {
                curID = game.gameID();
                curNames.add(game.gameName());
            }
            if (!curNames.contains(cgRequest.gameName())) {
                int newID = curID + 1;
                GameData newGame = new GameData(newID, null, null, cgRequest.gameName(), new ChessGame());
                dataAccess.addGame(newGame);
                return new CreateGameResponse(newID);
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
            String curUsername = auth.username();
            Integer gameID = jgRequest.gameID();
//        Do I need to add gameID checker? or is it based off the exception thrown
            GameData curGame = dataAccess.getGame(gameID);
            if (curGame == null){
                throw new DataAccessException("Error: bad request");
            }

            String newBlkUsername = curGame.blackUsername();
            String newWhtUsername = curGame.whiteUsername();
            System.out.println(jgRequest);
            System.out.println(curGame);
            if (jgRequest.playerColor() == ChessGame.TeamColor.BLACK) {
                if (newBlkUsername == null) {
                    newBlkUsername = curUsername;
                } else {
                    throw new DataAccessException("Error: already taken");
                }
            } else if (jgRequest.playerColor() == ChessGame.TeamColor.WHITE) {
                if (newWhtUsername == null) {
                    newWhtUsername = curUsername;
                }
                else {
                    throw new DataAccessException("Error: already taken");
                }
            } else {
                return;
            }
            GameData newGame = new GameData(gameID, newWhtUsername, newBlkUsername, curGame.gameName(), curGame.game());
            System.out.println(newGame);
            dataAccess.updateGame(gameID, newGame);
            System.out.println(dataAccess.listGame());
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
        ArrayList<AuthData> authList = new ArrayList<>(dataAccess.listAuth());
        ArrayList<String> authTokens = new ArrayList<>();
        for (AuthData curAuth : authList){
            authTokens.add(curAuth.authToken());
        }
        return authTokens.contains(authToken);
    }

}
