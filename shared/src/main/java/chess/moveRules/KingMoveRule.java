package chess.moveRules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class KingMoveRule {
    private final ArrayList<ChessMove> moveList;
    private final ChessPosition startPosition;
    private final ChessBoard board;

    public KingMoveRule(ChessPosition startPosition, ChessBoard board) {
        this.startPosition = startPosition;
        this.board = board;
        moveList = new ArrayList<>();
        setMoveList();
    }

    public void setMoveList(){
        int curRow = startPosition.getRow()-1;
        int curCol = startPosition.getColumn()-1;

        for(int i=curRow-1;i<=curRow+1;i++){
            for(int j=curCol-1;j<=curCol+1;j++){
                ChessPosition newPos = new ChessPosition(i+1,j+1);
                if (newPos.getColumn() < 8 && newPos.getColumn() >= 1){
                    if (newPos.getRow() < 8 && newPos.getRow() >= 1){
                        if (board.getPiece(newPos) == null){
                            ChessMove newMove = new ChessMove(startPosition,newPos,null);
                            moveList.add(newMove);
                        }
                        else if(board.getPiece(newPos).getTeamColor() != board.getPiece(startPosition).getTeamColor()){
                            ChessMove newMove = new ChessMove(startPosition,newPos,null);
                            moveList.add(newMove);
                        }
                    }
                }

            }
        }

    }

    public ArrayList<ChessMove> getMoveList(){ return moveList; }

}


