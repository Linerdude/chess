package chess.moveRules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class KnightMoveRule {
    private final ChessPosition startPosition;
    private final ChessBoard board;
    private ArrayList<ChessMove> moveList;

    public KnightMoveRule(ChessPosition startPosition, ChessBoard board) {
        this.startPosition = startPosition;
        this.board = board;
        moveList = new ArrayList<>();
        setMoveList();
    }

    public void setMoveList(){
        int cur_row = startPosition.getRow()-1;
        int cur_col = startPosition.getColumn()-1;

        addKnighMove(cur_row+2,cur_col+1);
        addKnighMove(cur_row-2,cur_col-1);
        addKnighMove(cur_row-2,cur_col+1);
        addKnighMove(cur_row+2,cur_col-1);
        addKnighMove(cur_row+1,cur_col+2);
        addKnighMove(cur_row-1,cur_col-2);
        addKnighMove(cur_row+1,cur_col-2);
        addKnighMove(cur_row-1,cur_col+2);

        ArrayList<ChessMove> newMoveList = new ArrayList<>(moveList);
        for(ChessMove i : moveList){
            if(i.getEndPosition().getRow()>8 || i.getEndPosition().getColumn()>8 || i.getEndPosition().getRow()<1 || i.getEndPosition().getColumn()<1 ){
                newMoveList.remove(i);
            } else if(board.getPiece(i.getEndPosition()) != null){
                if(board.getPiece(i.getEndPosition()).getTeamColor() == board.getPiece(startPosition).getTeamColor()){
                    newMoveList.remove(i);
                }

            }
        }

        moveList = newMoveList;

    }

    public void addKnighMove(int row, int col){
        ChessPosition new_pos = new ChessPosition(row+1, col+1);
        ChessMove new_move = new ChessMove(startPosition,new_pos,null);
        moveList.add(new_move);
    }

    public ArrayList<ChessMove> getMoveList(){ return moveList; }

}
