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
        int curRow = startPosition.getRow()-1;
        int curCol = startPosition.getColumn()-1;

        addKnighMove(curRow+2,curCol+1);
        addKnighMove(curRow-2,curCol-1);
        addKnighMove(curRow-2,curCol+1);
        addKnighMove(curRow+2,curCol-1);
        addKnighMove(curRow+1,curCol+2);
        addKnighMove(curRow-1,curCol-2);
        addKnighMove(curRow+1,curCol-2);
        addKnighMove(curRow-1,curCol+2);

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
        ChessPosition newPos = new ChessPosition(row+1, col+1);
        ChessMove newMove = new ChessMove(startPosition,newPos,null);
        moveList.add(newMove);
    }

    public ArrayList<ChessMove> getMoveList(){ return moveList; }

}
