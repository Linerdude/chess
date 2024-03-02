package chess.moveRules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class MoveRule {

    private final ArrayList<ChessMove> moveList;
    private final ChessPosition startPosition;
    private final ChessBoard board;

    public MoveRule(ChessPosition startPosition, ChessBoard board) {
        this.startPosition = startPosition;
        this.board = board;
        moveList = new ArrayList<>();
        setMoveList();
    }

    public void setMoveList(){
        MoveStraight(startPosition, board, moveList);
    }

    static void MoveStraight(ChessPosition startPosition, ChessBoard board, ArrayList<ChessMove> moveList) {
        int initRow = startPosition.getRow()-1;
        int initCol = startPosition.getColumn()-1;

        MoveStraightVer(startPosition, board, moveList, initRow, initCol, 1);
        MoveStraightHor(startPosition, board, moveList, initRow, initCol, 1);
        MoveStraightHor(startPosition, board, moveList, initRow, initCol, -1);
        MoveStraightVer(startPosition, board, moveList, initRow, initCol, -1);
    }

    private static void MoveStraightHor(ChessPosition startPosition, ChessBoard board, ArrayList<ChessMove> moveList, int curRow, int curCol, int i) {
        int endNum;
        if (i>0){
            endNum = 7;
        } else {
            endNum = 0;
        }
        while(curCol != endNum){
            curCol += i;
            if (moveItFunction(startPosition, board, moveList, curRow, curCol)) break;
        }

    }

    private static void MoveStraightVer(ChessPosition startPosition, ChessBoard board, ArrayList<ChessMove> moveList, int curRow, int curCol, int i) {
        int end;
        if (i<0){
            end = 0;
        } else {
            end = 7;
        }
        while(curRow != end){
            curRow += i;
            if (moveItFunction(startPosition, board, moveList, curRow, curCol)) break;
        }
    }

    private static boolean moveItFunction(ChessPosition startPosition, ChessBoard board, ArrayList<ChessMove> moveList, int curRow, int curCol) {
        ChessPosition newPos = new ChessPosition(curRow +1, curCol +1);
        if (board.getPiece(newPos) == null){
            ChessMove newMove = new ChessMove(startPosition,newPos,null);
            moveList.add(newMove);
        } else if (board.getPiece(newPos).getTeamColor() != board.getPiece(startPosition).getTeamColor()){
            ChessMove newMove = new ChessMove(startPosition,newPos,null);
            moveList.add(newMove);
            return true;
        } else return board.getPiece(newPos).getTeamColor() == board.getPiece(startPosition).getTeamColor();
        return false;
    }

}
