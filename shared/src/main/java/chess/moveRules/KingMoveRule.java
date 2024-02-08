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

        for(int i=cur_row-1;i<=cur_row+1;i++){
            for(int j=cur_col-1;j<=cur_col+1;j++){
                ChessPosition new_pos = new ChessPosition(i+1,j+1);
                if (new_pos.getColumn() < 8 && new_pos.getColumn() >= 1){
                    if (new_pos.getRow() < 8 && new_pos.getRow() >= 1){
                        if (board.getPiece(new_pos) == null){
                            ChessMove new_move = new ChessMove(startPosition,new_pos,null);
                            moveList.add(new_move);
                        }
                        else if(board.getPiece(new_pos).getTeamColor() != board.getPiece(startPosition).getTeamColor()){
                            ChessMove new_move = new ChessMove(startPosition,new_pos,null);
                            moveList.add(new_move);
                        }
                    }
                }

            }
        }
//        ArrayList<ChessMove> tempList = new ArrayList<ChessMove>();
//        for (ChessMove move : moveList){
//            if (move.getEndPosition().getColumn() > 7 || move.getEndPosition().getColumn() < 1){
//                if (move.getEndPosition().getRow() > 7 || move.getEndPosition().getRow() < 1){
//                    tempList.add(move);
//                }
//            }
//        }
//        moveList.removeAll(tempList);

    }

    public ArrayList<ChessMove> getMoveList(){ return moveList; }

}


