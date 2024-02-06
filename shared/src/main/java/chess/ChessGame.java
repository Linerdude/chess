package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard curBoard;
    private ChessBoard tempBoard;
    private TeamColor teamTurn;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece curPiece = curBoard.getPiece(startPosition);
        if(curPiece == null){
            return null;
        }
        Collection<ChessMove> curPieceMoves = curPiece.pieceMoves(curBoard,startPosition);
        ArrayList<ChessMove> validPieceMoves = new ArrayList<ChessMove>();
        for (ChessMove curMove : curPieceMoves){
            ChessPosition endPos = curMove.getEndPosition();
            if (curBoard.getPiece(endPos) == null){
                validPieceMoves.add(curMove);
            }
            else if (curBoard.getPiece(endPos).getTeamColor() != curBoard.getPiece(startPosition).getTeamColor()){
                validPieceMoves.add(curMove);
            }
        }
        return validPieceMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessBoard newBoard = new ChessBoard(curBoard);
//        int endRow = move.getEndPosition().getRow() - 1;
//        int endCol = move.getEndPosition().getColumn() - 1;

        ChessPiece curPiece = curBoard.getPiece(move.getStartPosition());
//        System.out.println(move.startPosition);
//        System.out.println(curBoard);

        TeamColor curTeamColor = curBoard.getPiece(move.startPosition).getTeamColor();
//        System.out.println(curBoard.getPiece(move.getStartPosition()));
        TeamColor otherTeam;
        if (curTeamColor == TeamColor.WHITE){
            otherTeam = TeamColor.BLACK;
        } else {
            otherTeam = TeamColor.WHITE;
        }

        newBoard.addPiece(move.endPosition, curPiece);
        newBoard.addPiece(move.startPosition,null);
        tempBoard = newBoard;

        if (isInCheck(curTeamColor) || teamTurn != curTeamColor){
//            System.out.println(teamTurn != curTeamColor);
            throw new InvalidMoveException();
        }
        curBoard = newBoard;

        setTeamTurn(otherTeam);

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        if (tempBoard == null){
            tempBoard = new ChessBoard(curBoard);
        }
        boolean inCheck = false;
        ChessPosition kingPos = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition curPos = new ChessPosition(i + 1, j + 1);
                ChessPiece curPiece = curBoard.getPiece(curPos);

                ChessPiece kingCompare = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
                if (Objects.equals(curPiece, kingCompare)){
                    kingPos = curPos;
                }
            }
        }

        TeamColor otherTeam;
        if (teamColor == TeamColor.WHITE){
            otherTeam = TeamColor.BLACK;
        } else {
            otherTeam = TeamColor.WHITE;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition curPos = new ChessPosition(i+1,j+1);
                ChessPiece curPiece = curBoard.getPiece(curPos);

                if (curPiece != null){
                    if (curPiece.getTeamColor() == otherTeam){
                        for (ChessMove move : curPiece.pieceMoves(tempBoard,curPos)){
                            ChessPosition endPos = move.getEndPosition();
                            if (endPos == kingPos) {
                                inCheck = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return inCheck;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        curBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return curBoard;
    }
}
