package chess.moveRules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class RookMoveRule {
    private final ArrayList<ChessMove> moveList;
    private final ChessPosition startPosition;
    private final ChessBoard board;

    public RookMoveRule(ChessPosition startPosition, ChessBoard board) {
        this.startPosition = startPosition;
        this.board = board;
        moveList = new ArrayList<>();
        setMoveList();
    }

    public void setMoveList(){
        int init_row = startPosition.getRow()-1;
        int init_col = startPosition.getColumn()-1;

        int cur_row = init_row;
        int cur_col = init_col;
        while(cur_row < 7){
            cur_row++;
            ChessPosition new_pos = new ChessPosition(cur_row+1,cur_col+1);
            if (board.getPiece(new_pos) == null){
                ChessMove new_move = new ChessMove(startPosition,new_pos,null);
                moveList.add(new_move);
            } else if (board.getPiece(new_pos).getTeamColor() != board.getPiece(startPosition).getTeamColor()){
                ChessMove new_move = new ChessMove(startPosition,new_pos,null);
                moveList.add(new_move);
                break;
            } else if (board.getPiece(new_pos).getTeamColor() == board.getPiece(startPosition).getTeamColor()){
                break;
            }
        }

        cur_row = init_row;
        cur_col = init_col;
        while(cur_row > 0){
            cur_row--;
            ChessPosition new_pos = new ChessPosition(cur_row+1,cur_col+1);
            if (board.getPiece(new_pos) == null){
                ChessMove new_move = new ChessMove(startPosition,new_pos,null);
                moveList.add(new_move);
            } else if (board.getPiece(new_pos).getTeamColor() != board.getPiece(startPosition).getTeamColor()){
                ChessMove new_move = new ChessMove(startPosition,new_pos,null);
                moveList.add(new_move);
                break;
            } else if (board.getPiece(new_pos).getTeamColor() == board.getPiece(startPosition).getTeamColor()){
                break;
            }
        }

        cur_row = init_row;
        cur_col = init_col;
        while(cur_col > 0){
            cur_col--;
            ChessPosition new_pos = new ChessPosition(cur_row+1,cur_col+1);
            if (board.getPiece(new_pos) == null){
                ChessMove new_move = new ChessMove(startPosition,new_pos,null);
                moveList.add(new_move);
            } else if (board.getPiece(new_pos).getTeamColor() != board.getPiece(startPosition).getTeamColor()){
                ChessMove new_move = new ChessMove(startPosition,new_pos,null);
                moveList.add(new_move);
                break;
            } else if (board.getPiece(new_pos).getTeamColor() == board.getPiece(startPosition).getTeamColor()){
                break;
            }
        }

        cur_row = init_row;
        cur_col = init_col;
        while(cur_col < 7){
            cur_col++;
            ChessPosition new_pos = new ChessPosition(cur_row+1,cur_col+1);
            if (board.getPiece(new_pos) == null){
                ChessMove new_move = new ChessMove(startPosition,new_pos,null);
                moveList.add(new_move);
            } else if (board.getPiece(new_pos).getTeamColor() != board.getPiece(startPosition).getTeamColor()){
                ChessMove new_move = new ChessMove(startPosition,new_pos,null);
                moveList.add(new_move);
                break;
            } else if (board.getPiece(new_pos).getTeamColor() == board.getPiece(startPosition).getTeamColor()){
                break;
            }
        }
    }

    public ArrayList<ChessMove> getMoveList(){ return moveList; }

}
