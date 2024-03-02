package chess.moveRules;

import chess.*;

import java.util.ArrayList;

public class PawnMoveRule {
    private ArrayList<ChessMove> moveList;
    private final ChessPosition startPosition;
    private final ChessBoard board;
//    private final ArrayList<ChessPiece.PieceType> promotion_pieces;

    public PawnMoveRule(ChessPosition startPosition, ChessBoard board) {
        this.startPosition = startPosition;
        this.board = board;
        moveList = new ArrayList<>();
        setMoveList();
//        promotion_pieces = ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK
    }

    public void setMoveList(){
        int curRow = startPosition.getRow()-1;
        int curCol = startPosition.getColumn()-1;

        int isWhite = 1;
        if(board.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
            isWhite = -1;
        }
        ChessPosition inFrontPos = new ChessPosition(curRow+1+isWhite,curCol+1);
        ChessMove inFrontMove = new ChessMove(startPosition,inFrontPos,null);
        if(board.getPiece(inFrontPos) == null){
            moveList.add(inFrontMove);
        }
        if((isWhite == 1 && startPosition.getRow() == 2) || (isWhite == -1 && startPosition.getRow() == 7)){
            ChessPosition inFrontDoublePos = new ChessPosition(curRow+1+(isWhite*2),curCol+1);
            ChessMove inFrontDoubleMove = new ChessMove(startPosition,inFrontDoublePos,null);
            if((board.getPiece(inFrontDoublePos) == null) && !moveList.isEmpty()){
                moveList.add(inFrontDoubleMove);
            }
        }
        if (isWhite == 1) {
            if (!(curCol > 6)) {
                moveForward(curRow, curCol, isWhite);
            }
            if (!(curCol < 1)) {
                moveForwardLeft(curRow, curCol, isWhite);
            }
        } else {
            if (!(curCol < 1)) {
                moveForward(curRow, curCol, isWhite);
            }
            if (!(curCol > 6)) {
                moveForwardLeft(curRow, curCol, isWhite);
            }
        }
        ArrayList<ChessMove> newMoveList = new ArrayList<>(moveList);
        for (ChessMove i : moveList){
            if(i.getEndPosition().getRow() == 1 || i.getEndPosition().getRow() == 8){
                newMoveList.remove(i);
                ChessMove newMoveQueen = new ChessMove(startPosition,i.getEndPosition(), ChessPiece.PieceType.QUEEN);
                ChessMove newMoveKnight = new ChessMove(startPosition,i.getEndPosition(), ChessPiece.PieceType.KNIGHT);
                ChessMove newMoveRook = new ChessMove(startPosition,i.getEndPosition(), ChessPiece.PieceType.ROOK);
                ChessMove newMoveBishop = new ChessMove(startPosition,i.getEndPosition(), ChessPiece.PieceType.BISHOP);
                newMoveList.add(newMoveQueen);
                newMoveList.add(newMoveKnight);
                newMoveList.add(newMoveRook);
                newMoveList.add(newMoveBishop);
            }
        }
        moveList = newMoveList;
    }

    private void moveForwardLeft(int curRow, int curCol, int isWhite) {
        ChessPosition inFrontLeftPos = new ChessPosition(curRow + 1 + isWhite, curCol + 1 - isWhite);
        if (board.getPiece(inFrontLeftPos) != null) {
            if (board.getPiece(inFrontLeftPos).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                ChessMove inFrontLeftMove = new ChessMove(startPosition, inFrontLeftPos, null);
                moveList.add(inFrontLeftMove);
            }
        }
    }

    private void moveForward(int curRow, int curCol, int isWhite) {
        ChessPosition inFrontRightPos = new ChessPosition(curRow + 1 + isWhite, curCol + 1 + isWhite);
        if (board.getPiece(inFrontRightPos) != null) {
            if (board.getPiece(inFrontRightPos).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                ChessMove inFrontRightMove = new ChessMove(startPosition, inFrontRightPos, null);
                moveList.add(inFrontRightMove);
            }
        }
    }

    public ArrayList<ChessMove> getMoveList(){ return moveList; }
}
