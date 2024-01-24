package chess.moveRules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class QueenMoveRule {
    private final ArrayList<ChessMove> moveList;
    private final ChessPosition startPosition;
    private final ChessBoard board;

    public QueenMoveRule(ChessPosition startPosition, ChessBoard board) {
        this.startPosition = startPosition;
        this.board = board;
        moveList = new ArrayList<>();
        setMoveList();
    }

    public void setMoveList(){
        int cur_row = startPosition.getRow()-1;
        int cur_col = startPosition.getColumn()-1;

        addRookMove(1,0,cur_row,cur_col);
        addRookMove(-1,0,cur_row,cur_col);
        addRookMove(0,1,cur_row,cur_col);
        addRookMove(0,-1,cur_row,cur_col);


        while(cur_row < 7 && cur_col < 7){
            cur_row++;
            cur_col++;
            ChessPosition new_position = new ChessPosition(cur_row+1,cur_col+1);
            if (board.getPiece(new_position) == null){
                ChessMove new_move = new ChessMove(startPosition,new_position,null);
                moveList.add(new_move);
            } else if (board.getPiece(new_position).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                ChessMove new_move = new ChessMove(startPosition,new_position,null);
                moveList.add(new_move);
                break;
            } else{
                break;
            }
        }

//      Bottom Left
        cur_row = startPosition.getRow()-1;
        cur_col = startPosition.getColumn()-1;

        while(cur_row > 0 && cur_col > 0){
            cur_row--;
            cur_col--;
            ChessPosition new_position = new ChessPosition(cur_row+1,cur_col+1);
            if (board.getPiece(new_position) == null){
                ChessMove new_move = new ChessMove(startPosition,new_position,null);
                moveList.add(new_move);
            } else if (board.getPiece(new_position).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                ChessMove new_move = new ChessMove(startPosition,new_position,null);
                moveList.add(new_move);
                break;
            }
            else{
                break;
            }
        }

//      Bottom Right
        cur_row = startPosition.getRow()-1;
        cur_col = startPosition.getColumn()-1;

        while(cur_row > 0 && cur_col < 7){
            cur_row--;
            cur_col++;
            ChessPosition new_position = new ChessPosition(cur_row+1,cur_col+1);
            if (board.getPiece(new_position) == null){
                ChessMove new_move = new ChessMove(startPosition,new_position,null);
                moveList.add(new_move);
            } else if (board.getPiece(new_position).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                ChessMove new_move = new ChessMove(startPosition,new_position,null);
                moveList.add(new_move);
                break;
            }
            else{
                break;
            }
        }

//      Top Left
        cur_row = startPosition.getRow()-1;
        cur_col = startPosition.getColumn()-1;

        while(cur_row < 7 && cur_col > 0){
            cur_row++;
            cur_col--;
            ChessPosition new_position = new ChessPosition(cur_row+1,cur_col+1);
            if (board.getPiece(new_position) == null){
                ChessMove new_move = new ChessMove(startPosition,new_position,null);
                moveList.add(new_move);
            } else if (board.getPiece(new_position).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                ChessMove new_move = new ChessMove(startPosition,new_position,null);
                moveList.add(new_move);
                break;
            }
            else{
                break;
            }
        }
    }

    public void addRookMove(int row_inc, int col_inc, int cur_row, int cur_col) {
        if(row_inc != 0) {
            while (cur_row < 7 && cur_row > 0) {
                cur_row = cur_row + row_inc;
                ChessPosition new_pos = new ChessPosition(cur_row + 1, cur_col + 1);
                if (board.getPiece(new_pos) == null) {
                    ChessMove new_move = new ChessMove(startPosition, new_pos, null);
                    moveList.add(new_move);
                } else if (board.getPiece(new_pos).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                    ChessMove new_move = new ChessMove(startPosition, new_pos, null);
                    moveList.add(new_move);
                    break;
                } else {
                    break;
                }
            }
        } else if(col_inc > 0){
            while (cur_col < 7) {
                cur_col = cur_col + col_inc;
                ChessPosition new_pos = new ChessPosition(cur_row + 1, cur_col + 1);
                if (board.getPiece(new_pos) == null) {
                    ChessMove new_move = new ChessMove(startPosition, new_pos, null);
                    moveList.add(new_move);
                } else if (board.getPiece(new_pos).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                    ChessMove new_move = new ChessMove(startPosition, new_pos, null);
                    moveList.add(new_move);
                    break;
                } else {
                    break;
                }
            }
        } else if (col_inc < 0){
            while (cur_col > 0) {
                cur_col = cur_col + col_inc;
                ChessPosition new_pos = new ChessPosition(cur_row + 1, cur_col + 1);
                if (board.getPiece(new_pos) == null) {
                    ChessMove new_move = new ChessMove(startPosition, new_pos, null);
                    moveList.add(new_move);
                } else if (board.getPiece(new_pos).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                    ChessMove new_move = new ChessMove(startPosition, new_pos, null);
                    moveList.add(new_move);
                    break;
                } else {
                    break;
                }
            }
        }
    }

    public ArrayList<ChessMove> getMoveList(){ return moveList; }

}
