package chess.moveRules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class BishopMoveRule {
    private final ArrayList<ChessMove> moveList;
    private final ChessPosition startPosition;
    private final ChessBoard board;

    public BishopMoveRule(ChessPosition startPosition, ChessBoard board) {
        this.startPosition = startPosition;
        this.board = board;
        moveList = new ArrayList<>();
        setMoveList();
    }

    public void setMoveList(){
//      Up Right
        int cur_row = startPosition.getRow()-1;
        int cur_col = startPosition.getColumn()-1;

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

    public ArrayList<ChessMove> getMoveList(){ return moveList; }

}


