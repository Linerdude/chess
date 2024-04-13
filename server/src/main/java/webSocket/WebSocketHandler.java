package webSocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.SQLDataAccess;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.userCommands.JoinPlayer;

import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import chess.moveRules.*;


import java.io.IOException;
import java.util.*;


@WebSocket
public class WebSocketHandler {

//    private final ConnectionManager connections = new ConnectionManager();
    private SQLDataAccess games;
    private SQLDataAccess auths;
    private SQLDataAccess users;
    private Map <Integer, ConnectionManager> gameOrganizer = new HashMap<>();
    //map gameID and connection manager
    private Map<Integer, List<String>> gamesAndUsers = new HashMap<>() {
    };
    public Map<Integer, String> gameOver = new HashMap<>();

    public WebSocketHandler(SQLDataAccess games, SQLDataAccess auths, SQLDataAccess users){
        System.out.println("WSH init");
        this.auths = auths;
        this.games = games;
        this.users = users;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand curUserGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (curUserGameCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(message, session);
            case JOIN_OBSERVER -> joinObserver(message, session);
            case MAKE_MOVE -> makeMove(message,session);
            case LEAVE -> leave(message,session);
            case RESIGN -> resign(message,session);
        }
    }

    public void joinPlayer(String msg, Session session){
        try {
            JoinPlayer join = new Gson().fromJson(msg, JoinPlayer.class);
            String authToken = join.getAuthString();
            AuthData auth = auths.getAuth(authToken);
            if (gameOrganizer.containsKey(join.gameID)){
                ConnectionManager con = gameOrganizer.get(join.gameID);
                con.add(join.getAuthString(),session);
            }else{
                ConnectionManager con = new ConnectionManager();
                con.add(join.getAuthString(),session);
                gameOrganizer.put(join.gameID, con);
            }
            if(auth != null) {
                GameData gameInfo = games.getGame(join.gameID);
                if (gameInfo != null) {
                    if(join.playerColor == ChessGame.TeamColor.WHITE) {
                        if (Objects.equals(auth.username(), gameInfo.whiteUsername())) {
                            JoinPlayerManager(auth, join, gameInfo, authToken);
                        }else{
                            Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "player hasn't joined game properly");
                            ConnectionManager c = gameOrganizer.get(join.gameID);
                            c.clientNotify(auth.authToken(), notification);
                            System.out.println("player hasn't joined correctly");
                        }
                    }else {
                        if (Objects.equals(auth.username(), gameInfo.blackUsername())) {
                            JoinPlayerManager(auth, join, gameInfo, authToken);
                        } else {
                            ConnectionManager c = gameOrganizer.get(join.gameID);
                            Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "player hasn't joined game properly");
                            c.clientNotify(auth.authToken(), notification);
                            System.out.println("player hasn't joined correctly");
                        }
                    }
                } else {
                    ConnectionManager c = gameOrganizer.get(join.gameID);
                    Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Bad gameID");
                    c.clientNotify(auth.authToken(), notification);
                    System.out.println("bad gameID");
                }
            }else{
                ConnectionManager c = gameOrganizer.get(join.gameID);
                Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Bad authtoken");
                c.clientNotify(authToken,notification);
                System.out.println("bad auth");
            }
        } catch (IOException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    private void JoinPlayerManager(AuthData auth, JoinPlayer join, GameData gameInfo, String authToken) throws IOException {
        String message = String.format("%s joined as %s player", auth.username(), join.playerColor);
        LoadGame notification = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameInfo);
        List<String>clientsInGame =  gamesAndUsers.get(gameInfo.gameID());
        if (clientsInGameCheck(clientsInGame)){
            clientsInGame = gamesAndUsers.get(gameInfo.gameID());
        }else{
            clientsInGame = new ArrayList<>();
        }
        clientsInGame.add(authToken);
        gamesAndUsers.put(gameInfo.gameID(), clientsInGame);
        Notification notif = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        ConnectionManager c = gameOrganizer.get(join.gameID);
        c.broadcast(auth.authToken(), notif);
        c.clientNotify(auth.authToken(), notification);
    }

    private void joinObserver(String msg, Session session) {
        try {
            JoinObserver observerCom = new Gson().fromJson(msg, JoinObserver.class);
            AuthData userInfo = auths.getAuth(observerCom.getAuthString());
            if (gameOrganizer.containsKey(observerCom.gameID)){
                ConnectionManager con = gameOrganizer.get(observerCom.gameID);
                con.add(observerCom.getAuthString(),session);
            }else{
                ConnectionManager con = new ConnectionManager();
                con.add(observerCom.getAuthString(),session);
                gameOrganizer.put(observerCom.gameID, con);
            }
            if(userInfo != null) {
                GameData gameInfo = games.getGame(observerCom.gameID);
                if(gameInfo != null) {
                    String message = String.format("%s joined as observer", userInfo.username());
                    LoadGame notification = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameInfo);
                    ConnectionManager c = gameOrganizer.get(observerCom.gameID);
                    c.clientNotify(userInfo.authToken(), notification);
                    Notification notif = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                    List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
                    if (clientsInGameCheck(clientsInGame)){
                        clientsInGame = gamesAndUsers.get(gameInfo.gameID());
                    }else{
                        clientsInGame = new ArrayList<>();
                    }
                    clientsInGame.add(observerCom.getAuthString());
                    gamesAndUsers.put(gameInfo.gameID(), clientsInGame);
                    c.broadcast(userInfo.authToken(), notif);
                }else{
                    ConnectionManager c = gameOrganizer.get(observerCom.gameID);
                    Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Bad gameID");
                    c.clientNotify(userInfo.authToken(), notification);
                }
            }else{
                ConnectionManager c = gameOrganizer.get(observerCom.gameID);
                Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Bad authtoken");
                c.clientNotify(observerCom.getAuthString(), notification);
            }
        } catch (IOException | DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }

    ;

    private void leave(String msg, Session session) {
        try {
            Leave leaveCommand = new Gson().fromJson(msg, Leave.class);
            String message;
            GameData gameInfo = games.getGame(leaveCommand.gameID);
            AuthData userInfo = auths.getAuth(leaveCommand.getAuthString());
            if (Objects.equals(userInfo.username(), gameInfo.blackUsername())) {
                games.removeUser(gameInfo, ChessGame.TeamColor.BLACK);
                message = String.format("%s stopped playing as the black user", userInfo.username());
                Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                ConnectionManager c = gameOrganizer.get(gameInfo.gameID());
                c.broadcast(userInfo.authToken(), notification);
                c.remove(leaveCommand.getAuthString());
                List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
                clientsInGame.remove((leaveCommand.getAuthString()));
                gamesAndUsers.put(gameInfo.gameID(), clientsInGame);

            } else if (Objects.equals(userInfo.username(), gameInfo.whiteUsername())) {
                games.removeUser(gameInfo, ChessGame.TeamColor.WHITE);
                message = String.format("%s stopped playing as the white user", userInfo.username());
                Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                ConnectionManager c = gameOrganizer.get(gameInfo.gameID());
                c.broadcast(userInfo.authToken(), notification);
                c.remove(leaveCommand.getAuthString());
                List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
                clientsInGame.remove((leaveCommand.getAuthString()));
                gamesAndUsers.put(gameInfo.gameID(), clientsInGame);
            }
            else {
                message = String.format("%s stopped observing the game", userInfo.username());
                Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                ConnectionManager c = gameOrganizer.get(gameInfo.gameID());
                c.broadcast(userInfo.authToken(), notification);
                c.remove(leaveCommand.getAuthString());
                List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
                clientsInGame.remove((leaveCommand.getAuthString()));
                gamesAndUsers.put(gameInfo.gameID(), clientsInGame);
            }
        } catch (IOException | DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }


    private void resign(String msg, Session session) {
        try {
            Resign resignCommand = new Gson().fromJson(msg, Resign.class);
            AuthData userInfo = auths.getAuth(resignCommand.getAuthString());
            GameData gameInfo = games.getGame(resignCommand.gameID);
            if(gameInfo.game().isGameOver){
                Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Can't resign, game is over");
                ConnectionManager c = gameOrganizer.get(gameInfo.gameID());
                c.clientNotify(userInfo.authToken(), notification);
                return;
            }
            List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
            if(!clientsInGameCheck(clientsInGame)){
                Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Can't resign, leave instead");
                ConnectionManager c = gameOrganizer.get(gameInfo.gameID());
                c.clientNotify(userInfo.authToken(), notification);
                return;
            }
            Iterator<String> iterator = clientsInGame.iterator();
            while (iterator.hasNext()) {
                String auth = iterator.next();
                if (Objects.equals(auth, userInfo.authToken())) {
                    gameInfo.game().setGameOver(true);
                    games.updateGame(gameInfo.gameID(), gameInfo);
                    String black = gameInfo.blackUsername();
                    String white = gameInfo.whiteUsername();
                    if(!Objects.equals(userInfo.username(), black) && !Objects.equals(userInfo.username(), white)){
                        Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Can't resign as observer");
                        ConnectionManager c = gameOrganizer.get(gameInfo.gameID());
                        c.clientNotify(userInfo.authToken(), notification);
                        break;
                    }
                    iterator.remove();
                    String message = String.format("%s resigned from the game. The game is over", userInfo.username());
                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                    ConnectionManager c = gameOrganizer.get(gameInfo.gameID());
                    c.broadcast(userInfo.authToken(), notification);
                    c = gameOrganizer.get(gameInfo.gameID());
                    c.clientNotify(userInfo.authToken(), notification);
                    c.remove(userInfo.authToken());
                    gamesAndUsers.put(gameInfo.gameID(), clientsInGame);
                }
            }
        } catch (IOException | DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }

    private boolean validateMove(GameData gameInfo, AuthData userInfo, MakeMove moveCommand) throws IOException {
        if(!Objects.equals(userInfo.username(), gameInfo.blackUsername()) && !Objects.equals(userInfo.username(), gameInfo.whiteUsername())){
            return false;
        }
        if(gameInfo.game().getTeamTurn() != (userInfo.username().equals(gameInfo.blackUsername()) ? ChessGame.TeamColor.BLACK: ChessGame.TeamColor.WHITE)){
            return false;
        }
        ChessPosition start = moveCommand.move.getStartPosition();
        Collection<ChessMove> validMoves = gameInfo.game().validMoves(start);
        for (ChessMove move : validMoves) {
            if (move.equals(moveCommand.move)) {
                return true;
            }
        }
        return false;
    }


    private void makeMove(String msg, Session session) throws IOException {
        try {
            MakeMove moveCommand = new Gson().fromJson(msg, MakeMove.class);
            AuthData userInfo = auths.getAuth(moveCommand.getAuthString());
            GameData gameInfo = games.getGame(moveCommand.gameID);
            if(gameInfo.game().isGameOver){
                Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Can't make move, game is over");
                ConnectionManager c = gameOrganizer.get(gameInfo.gameID());
                c.clientNotify(userInfo.authToken(), notification);
                return;
            }
            if(!validateMove(gameInfo,userInfo,moveCommand)) {
                String errorMsg;
                if(!Objects.equals(userInfo.username(), gameInfo.blackUsername()) && !Objects.equals(userInfo.username(), gameInfo.whiteUsername())){
                    errorMsg = "Cannot make move as an observer";
                }
                else{
                    errorMsg = "Not your turn, cannot make move";
                }
                Error notification = new Error(ServerMessage.ServerMessageType.ERROR, errorMsg);
                ConnectionManager c = gameOrganizer.get(gameInfo.gameID());
                c.clientNotify(moveCommand.getAuthString(), notification);
            }else{
                gameInfo.game().makeMove(moveCommand.move);
                games.updateGame(gameInfo.gameID(),gameInfo);
                String message = String.format("%s made a move", userInfo.username());
                Notification noti = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                LoadGame notification = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameInfo);
                ConnectionManager c = gameOrganizer.get(gameInfo.gameID());
                c.broadcast(userInfo.authToken(), notification);
                c.clientNotify(userInfo.authToken(), notification);
                c.broadcast(userInfo.authToken(), noti);
            }

        }
        catch (IOException | InvalidMoveException |
               DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }

    public boolean clientsInGameCheck(List<String> clientsInGame){
        if(clientsInGame != null) {
            return true;
        }
        return false;
    }

}
