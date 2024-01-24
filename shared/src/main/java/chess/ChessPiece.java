package chess;

import chess.moveRules.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece that)) return false;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<ChessMove>();
        switch(type){
            case KING:
                KingMoveRule cur_king = new KingMoveRule(myPosition,board);
                moveList = cur_king.getMoveList();
                break;
            case QUEEN:
                QueenMoveRule cur_queen = new QueenMoveRule(myPosition,board);
                moveList = cur_queen.getMoveList();
                break;
            case BISHOP:
                BishopMoveRule cur_bishop = new BishopMoveRule(myPosition,board);
                moveList = cur_bishop.getMoveList();
                break;
            case KNIGHT:
                KnightMoveRule cur_knight = new KnightMoveRule(myPosition,board);
                moveList = cur_knight.getMoveList();
                break;
            case ROOK:
                RookMoveRule cur_rook = new RookMoveRule(myPosition,board);
                moveList = cur_rook.getMoveList();
                break;
            case PAWN:
                PawnMoveRule cur_pawn = new PawnMoveRule(myPosition,board);
                moveList = cur_pawn.getMoveList();
                break;
        }

        return moveList;
    }
}
