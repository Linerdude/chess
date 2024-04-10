package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand{
    public int gameID;
    public ChessMove move;
    public MakeMove(String authToken, int gameID, ChessMove move) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;

    }
}
