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

    public Boolean isGameOver;

    public ChessGame() {
        isGameOver = false;
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

    public void setGameOver(Boolean isOver){
        isGameOver = isOver;
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

        ChessBoard validOldBoard = new ChessBoard(curBoard);
        ChessBoard newBoard = new ChessBoard(curBoard);


        ChessPiece curPiece = curBoard.getPiece(myPosition);
        if(curPiece == null){
            return null;
        }

        HashSet<ChessMove> curPieceMoves = (HashSet<ChessMove>) curPiece.pieceMoves(curBoard,myPosition);

        HashSet<ChessMove> validPieceMoves = new HashSet<>();
        for (ChessMove curMove : curPieceMoves){
            ChessPosition endPos = curMove.getEndPosition();
            ChessPiece endPiece = curBoard.getPiece(endPos);

            TeamColor curTeamColor = curBoard.getPiece(curMove.startPosition).getTeamColor();
            curBoard.addPiece(curMove.endPosition, curPiece);
            curBoard.addPiece(curMove.startPosition,null);

//            System.out.println(curBoard);

            if (isInCheck(curTeamColor)){
//                System.out.println(curBoard);
//                System.out.println("move will result in check");

                curBoard.addPiece(curMove.endPosition,endPiece);
                curBoard.addPiece(curMove.startPosition,curPiece);
                continue;
            }

            curBoard.addPiece(curMove.endPosition,endPiece);
            curBoard.addPiece(curMove.startPosition,curPiece);
            if (curBoard.getPiece(endPos) == null){
                validPieceMoves.add(curMove);
            } else if (curBoard.getPiece(endPos).getTeamColor() != curBoard.getPiece(startPosition).getTeamColor()){
                validPieceMoves.add(curMove);
            }
            curBoard.addPiece(curMove.endPosition,endPiece);
            curBoard.addPiece(curMove.startPosition,curPiece);
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
        if (!curMoves.contains(move)){
            throw new InvalidMoveException();
        }
        ChessBoard newBoard = new ChessBoard(curBoard);
        ChessBoard oldBoard = new ChessBoard(curBoard);

        ChessPiece curPiece = curBoard.getPiece(move.getStartPosition());


        TeamColor curTeamColor = curBoard.getPiece(move.startPosition).getTeamColor();

        if (move.getPromotionPiece() != null){
            ChessPiece.PieceType newType = move.getPromotionPiece();
            curPiece = new ChessPiece(curTeamColor,newType);
        }
        TeamColor otherTeam;
        if (curTeamColor == TeamColor.WHITE){
            otherTeam = TeamColor.BLACK;
        } else {
            otherTeam = TeamColor.WHITE;
        }

        newBoard.addPiece(move.endPosition, curPiece);
        newBoard.addPiece(move.startPosition,null);
        curBoard = new ChessBoard(newBoard);

        if (isInCheck(curTeamColor) || teamTurn != curTeamColor){
            curBoard = new ChessBoard(oldBoard);
            throw new InvalidMoveException();
        }

        setTeamTurn(otherTeam);

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessBoard oldBoard = new ChessBoard(curBoard);
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
        curBoard = oldBoard;
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
                                curBoard = new ChessBoard(oldBoard);
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
