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
        int cur_row = startPosition.getRow()-1;
        int cur_col = startPosition.getColumn()-1;

        for(int i=cur_row-1;i<=cur_row+1 && i>0 && i<7;i++){
            for(int j=cur_col-1;j<=cur_col+1 && j>0 && j<7;j++){
                ChessPosition new_pos = new ChessPosition(i+1,j+1);
                if (board.getPiece(new_pos) == null){
                    ChessMove new_move = new ChessMove(startPosition,new_pos,null);
                    moveList.add(new_move);
                } else if(board.getPiece(new_pos).getTeamColor() != board.getPiece(startPosition).getTeamColor()){
                    ChessMove new_move = new ChessMove(startPosition,new_pos,null);
                    moveList.add(new_move);
                }
            }
        }
    }

    public ArrayList<ChessMove> getMoveList(){ return moveList; }

}


