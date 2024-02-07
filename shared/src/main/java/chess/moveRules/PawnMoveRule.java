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
        int cur_row = startPosition.getRow()-1;
        int cur_col = startPosition.getColumn()-1;

        int is_white = 1;
        if(board.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
            is_white = -1;
        }
        ChessPosition in_front_pos = new ChessPosition(cur_row+1+is_white,cur_col+1);
        ChessMove in_front_move = new ChessMove(startPosition,in_front_pos,null);
        if(board.getPiece(in_front_pos) == null){
            moveList.add(in_front_move);
        }
        if((is_white == 1 && startPosition.getRow() == 2) || (is_white == -1 && startPosition.getRow() == 7)){
            ChessPosition in_front_double_pos = new ChessPosition(cur_row+1+(is_white*2),cur_col+1);
            ChessMove in_front_double_move = new ChessMove(startPosition,in_front_double_pos,null);
            if((board.getPiece(in_front_double_pos) == null) && !moveList.isEmpty()){
                moveList.add(in_front_double_move);
            }
        }
        if (is_white == 1) {
            if (!(cur_col > 6)) {
                ChessPosition in_front_right_pos = new ChessPosition(cur_row + 1 + is_white, cur_col + 1 + is_white);
                if (board.getPiece(in_front_right_pos) != null) {
                    if (board.getPiece(in_front_right_pos).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                        ChessMove in_front_right_move = new ChessMove(startPosition, in_front_right_pos, null);
                        moveList.add(in_front_right_move);
                    }
                }
            }
            if (!(cur_col < 1)) {
                ChessPosition in_front_left_pos = new ChessPosition(cur_row + 1 + is_white, cur_col + 1 - is_white);
                if (board.getPiece(in_front_left_pos) != null) {
                    if (board.getPiece(in_front_left_pos).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                        ChessMove in_front_left_move = new ChessMove(startPosition, in_front_left_pos, null);
                        moveList.add(in_front_left_move);
                    }
                }
            }
        } else {
            if (!(cur_col < 1)) {
                ChessPosition in_front_right_pos = new ChessPosition(cur_row + 1 + is_white, cur_col + 1 + is_white);
                if (board.getPiece(in_front_right_pos) != null) {
                    if (board.getPiece(in_front_right_pos).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                        ChessMove in_front_right_move = new ChessMove(startPosition, in_front_right_pos, null);
                        moveList.add(in_front_right_move);
                    }
                }
            }
            if (!(cur_col > 6)) {
                ChessPosition in_front_left_pos = new ChessPosition(cur_row + 1 + is_white, cur_col + 1 - is_white);
                if (board.getPiece(in_front_left_pos) != null) {
                    if (board.getPiece(in_front_left_pos).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                        ChessMove in_front_left_move = new ChessMove(startPosition, in_front_left_pos, null);
                        moveList.add(in_front_left_move);
                    }
                }
            }
        }
        ArrayList<ChessMove> newMoveList = new ArrayList<>(moveList);
        for (ChessMove i : moveList){
            if(i.getEndPosition().getRow() == 1 || i.getEndPosition().getRow() == 8){
                newMoveList.remove(i);
                ChessMove new_move_queen = new ChessMove(startPosition,i.getEndPosition(), ChessPiece.PieceType.QUEEN);
                ChessMove new_move_knight = new ChessMove(startPosition,i.getEndPosition(), ChessPiece.PieceType.KNIGHT);
                ChessMove new_move_rook = new ChessMove(startPosition,i.getEndPosition(), ChessPiece.PieceType.ROOK);
                ChessMove new_move_bishop = new ChessMove(startPosition,i.getEndPosition(), ChessPiece.PieceType.BISHOP);
                newMoveList.add(new_move_queen);
                newMoveList.add(new_move_knight);
                newMoveList.add(new_move_rook);
                newMoveList.add(new_move_bishop);
            }
        }
        moveList = newMoveList;
    }
    public ArrayList<ChessMove> getMoveList(){ return moveList; }
}
