package chess;

import chess.moveRules.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.HashSet;

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

    public ChessPiece(ChessPiece chessPieceCopy) {
        this.pieceColor = chessPieceCopy.pieceColor;
        this.type = chessPieceCopy.type;
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
    public HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();

        switch (type) {
            case KING -> {
                KingMoveRule curKing = new KingMoveRule(myPosition, board);
                moves.addAll(curKing.getMoveList());
            }
            case QUEEN -> {
                QueenMoveRule curQueen = new QueenMoveRule(myPosition, board);
                moves.addAll(curQueen.getMoveList());
            }
            case BISHOP -> {
                BishopMoveRule curBishop = new BishopMoveRule(myPosition, board);
                moves.addAll(curBishop.getMoveList());
            }
            case KNIGHT -> {
                KnightMoveRule curKnight = new KnightMoveRule(myPosition, board);
                moves.addAll(curKnight.getMoveList());
            }
            case ROOK -> {
                RookMoveRule curRook = new RookMoveRule(myPosition, board);
                moves.addAll(curRook.getMoveList());
            }
            case PAWN -> {
                PawnMoveRule curPawn = new PawnMoveRule(myPosition, board);
                moves.addAll(curPawn.getMoveList());
            }
        }

        return moves;
    }
}
