package chess;

import java.util.HashSet;

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
    public HashSet<ChessMove> validMoves(ChessPosition startPosition) {
        int curCol = startPosition.getColumn();
        int curRow = startPosition.getRow();

        ChessPosition myPosition = new ChessPosition(curRow,curCol);

        ChessPiece curPiece = curBoard.getPiece(myPosition);
        if(curPiece == null){
            return null;
        }



        System.out.println(curPiece);
        System.out.println(startPosition);
        System.out.println(curBoard);
        HashSet<ChessMove> curPieceMoves = (HashSet<ChessMove>) curPiece.pieceMoves(curBoard,myPosition);
        System.out.println(curPieceMoves);

        HashSet<ChessMove> validPieceMoves = new HashSet<>();
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

        HashSet<ChessMove> curMoves = validMoves(move.getStartPosition());
        System.out.println(curMoves);
        System.out.println(move);
        if (!curMoves.contains(move)){
            return;
        }
        ChessBoard newBoard = new ChessBoard(curBoard);

        ChessPiece curPiece = curBoard.getPiece(move.getStartPosition());

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

                if (curPiece != null) {
                    if (curPiece.getPieceType() == ChessPiece.PieceType.KING && curPiece.getTeamColor() == teamColor) {
//                        System.out.println("Found King");
                        kingPos = curPos;
                        break;
                    }
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

                if (curPiece != null && kingPos != null){

                    if (curPiece.getTeamColor() == otherTeam){
                        HashSet<ChessMove> curMoves = (HashSet<ChessMove>) curPiece.pieceMoves(curBoard,curPos);
                        for (ChessMove move : curMoves){
                            ChessPosition endPos = move.getEndPosition();
                            if (endPos.getRow() == kingPos.getRow() && endPos.getColumn() == kingPos.getColumn()) {
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
        boolean inCheckmate = true;
        if (!isInCheck(teamColor)){
            return false;
        } else {


//            ChessPosition kingPos = null;
//            for (int i = 0; i < 8; i++) {
//                for (int j = 0; j < 8; j++) {
//                    ChessPosition curPos = new ChessPosition(i + 1, j + 1);
//                    ChessPiece curPiece = curBoard.getPiece(curPos);
//
//                    ChessPiece kingCompare = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
//                    if (Objects.equals(curPiece, kingCompare)) {
//                        kingPos = curPos;
//                    }
//                }
//            }
            ChessBoard oldBoard = new ChessBoard(curBoard);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ChessPosition curPos = new ChessPosition(i+1,j+1);
                    ChessPiece curPiece = curBoard.getPiece(curPos);

                    if (curPiece != null){
                        if (curPiece.getTeamColor() == teamColor){
                            for (ChessMove move : curPiece.pieceMoves(curBoard,curPos)){
//                                System.out.println(move);
                                try {
                                    makeMove(move);
                                } catch (InvalidMoveException e) {

                                }
                                if (!isInCheck(teamColor)) {
                                    inCheckmate = false;
                                    break;
                                }
                                curBoard = oldBoard;
                            }
                        }
                    }
                }
            }

        }
        return inCheckmate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean inStale = false;
        if (teamTurn == teamColor) {
            inStale = true;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ChessPosition curPos = new ChessPosition(i + 1, j + 1);
                    ChessPiece curPiece = curBoard.getPiece(curPos);

                    int counter = 0;
                    int invalidCounter = 0;
                    if (curPiece != null) {
                        if (curPiece.getTeamColor() == teamColor) {
                            for (ChessMove move : curPiece.pieceMoves(curBoard, curPos)) {
                                try {
                                    counter++;
                                    makeMove(move);
                                } catch (InvalidMoveException e) {
                                    invalidCounter++;
                                }
                                System.out.println(counter);
                                System.out.println(invalidCounter);
                                if (counter > invalidCounter) {
                                    inStale = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return inStale;
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
