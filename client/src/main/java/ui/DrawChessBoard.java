package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import java.util.*;

import static java.lang.Math.abs;
import static ui.EscapeSequences.*;

public class DrawChessBoard {

    static String[] letterHeaders = {"   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "};
    static String[] letterHeadersForward = {"   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "};
    static String[] letterHeadersReverse = {"   ", " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", "   "};

    static String king = " K ";
    static String queen = " Q ";
    static String knight = " N ";
    static String bishop = " B ";
    static String rook = " R ";
    static String pawn = " P ";

    private static String boardColor1 = SET_BG_COLOR_LIGHT_GREY;
    private static String boardColor2 = SET_BG_COLOR_DARK_GREY;
    private static final String teamText1 = SET_TEXT_COLOR_BLACK;
    private static final String teamText2 = SET_TEXT_COLOR_WHITE;
    private static final String outerBoarderColor = SET_BG_COLOR_WHITE;
    private static final String outerBoarderTextColor = SET_TEXT_COLOR_BLACK;
    private static final String blank = "\u001B[0m";

    private static int directionIndicator = 0;

    public static void chessBoardToTerminal(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        chessBoardReverse(out);
        blankLine(out);
        blankLine(out);
        chessBoardForward(out);
        blankLine(out);

    }

    public static void printCurBoard(ChessGame chessGame, String teamColor) {
        printCurBoard(chessGame,teamColor,null);
    }


        public static void printCurBoard(ChessGame chessGame, String teamColor, int[] pos){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        directionIndicator = 1;
        letterHeaders = letterHeadersForward;

        int [][] validMoveArray = new int[8][8];
        ChessBoard chessBoard = new ChessBoard(chessGame.getBoard());

        if (pos != null) {
            ChessPosition curPosition = new ChessPosition(pos[0]+1,pos[1]+1);
//            ChessPiece curPiece = chessBoard.getPiece(curPosition);
//            HashSet<ChessMove> curMoves =  curPiece.pieceMoves(chessBoard,curPosition);
            HashSet<ChessMove> curMoves = chessGame.validMoves(curPosition);
            for (ChessMove move : curMoves){
                ChessPosition endPos = move.getEndPosition();
                validMoveArray[endPos.getRow()-1][endPos.getColumn()-1] = 1;
            }
            validMoveArray[pos[0]][pos[1]] = 2;
        }

        int[] rowNum;
        if (Objects.equals(teamColor, "WHITE")){
            rowNum = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
            chessBoard = flipBoard(chessBoard);
            validMoveArray = flipArray(validMoveArray);
            directionIndicator = 0;

        }
        else {
            rowNum = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
            letterHeaders = letterHeadersReverse;
        }

        printString(out,null,null,"header", null);

        for (int i = 0; i <= 7; i++){
            ArrayList<String> curRow = new ArrayList<>();
            ArrayList<Integer> curColor = new ArrayList<>();

            for (int j = 0; j<=7; j++){
                ChessPosition newPosition = new ChessPosition(i+1,j+1);
                ChessPiece curPiece = chessBoard.getPiece(newPosition);
                String curPieceString = "";

                if (curPiece!= null){

                    switch (curPiece.getPieceType()) {
                        case ROOK -> {
                            curPieceString = rook;
                            break;
                        }
                        case KNIGHT -> {
                            curPieceString = knight;
                            break;
                        }
                        case QUEEN -> {
                            curPieceString = queen;
                            break;
                        }
                        case BISHOP -> {
                            curPieceString = bishop;
                            break;
                        }
                        case PAWN -> {
                            curPieceString = pawn;
                            break;
                        }
                        case KING -> {
                            curPieceString = king;
                            break;
                        }
                        case null -> {
                            curPieceString = "   ";
                            break;
                        }
                    }
                } else {
                    curPieceString = "   ";
                }
                if (curPiece != null) {
                    if (curPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        curColor.add(0);
                    } else {
                        curColor.add(1);
                    }
                } else {
                    curColor.add(1);
                }
                curRow.add(curPieceString);
            }
            String[] rowToPrint = curRow.toArray(new String[0]);
            Integer[] colorToPrint = curColor.toArray(new Integer[0]);
            printString(out,rowToPrint,colorToPrint,"layer",rowNum[i], validMoveArray[i]);
        }
        printString(out,null,null,"header", null);
        blankLine(out);
    }

    public static void chessBoardForward(PrintStream out){
        directionIndicator = 0;
        letterHeaders = letterHeadersForward;
        printString(out,null,null,"header", null);
        String[] startingLineArray = {rook, knight, bishop, queen, king, bishop, knight, rook};
        Integer[] colorArrayWhite = {0,0,0,0,0,0,0,0};
        Integer[] colorArrayBlack = {1,1,1,1,1,1,1,1};
        printString(out,startingLineArray, colorArrayWhite,"layer", 1);
        String[] startingPawnArray = {pawn, pawn, pawn, pawn, pawn, pawn, pawn, pawn};
        printString(out,startingPawnArray,colorArrayWhite,"layer", 2);
        String[] emptyRowArray = {"   ","   ","   ","   ","   ","   ","   ","   "};
        printString(out,emptyRowArray,colorArrayWhite,"layer", 3);
        printString(out,emptyRowArray,colorArrayWhite,"layer", 4);
        printString(out,emptyRowArray,colorArrayBlack,"layer", 5);
        printString(out,emptyRowArray,colorArrayBlack,"layer", 6);
        printString(out,startingPawnArray,colorArrayBlack,"layer", 7);
        printString(out,startingLineArray,colorArrayBlack,"layer", 8);
        printString(out,null,null,"header", null);
    }

    public static void chessBoardReverse(PrintStream out){
        directionIndicator = 1;
        letterHeaders = letterHeadersReverse;
        printString(out,null,null,"header", null);
        String[] startingLineArray = {rook, knight, bishop, king, queen, bishop, knight, rook};
        Integer[] colorArrayWhite = {0,0,0,0,0,0,0,0};
        Integer[] colorArrayBlack = {1,1,1,1,1,1,1,1};
        printString(out,startingLineArray, colorArrayBlack,"layer", 8);
        String[] startingPawnArray = {pawn, pawn, pawn, pawn, pawn, pawn, pawn, pawn};
        printString(out,startingPawnArray,colorArrayBlack,"layer", 7);
        String[] emptyRowArray = {"   ","   ","   ","   ","   ","   ","   ","   "};
        printString(out,emptyRowArray,colorArrayBlack,"layer", 6);
        printString(out,emptyRowArray,colorArrayBlack,"layer", 5);
        printString(out,emptyRowArray,colorArrayWhite,"layer", 4);
        printString(out,emptyRowArray,colorArrayWhite,"layer", 3);
        printString(out,startingPawnArray,colorArrayWhite,"layer", 2);
        printString(out,startingLineArray,colorArrayWhite,"layer", 1);
        printString(out,null,null,"header", null);
    }

    private static void printString(PrintStream out, String[] toPrint, Integer[] colorArray, String layer, Integer sideNum){
        printString(out,toPrint,colorArray,layer,sideNum, null);
    }

    private static void printString(PrintStream out, String[] toPrint, Integer[] colorArray, String layer, Integer sideNum, int[] isValidMove){
        if(Objects.equals(layer, "header")){
            StringBuilder headerToPrint = new StringBuilder();
            for (String letterHeader : letterHeaders) {
                headerToPrint.append(letterHeader);
            }
            String newHeader = String.valueOf(headerToPrint);
            outerBoarderSet(out);
            out.print(newHeader);
            blankLine(out);
        }
        if(Objects.equals(layer, "layer")) {
            outerBoarderSet(out);
            out.print(" " + sideNum.toString() + " ");
            int counter = 0;
            int evenOrOdd = sideNum % 2;
            for (String curPiece : toPrint) {
                if (isValidMove[counter] == 1){
                    boardColor1 = SET_BG_COLOR_GREEN;
                    boardColor2 = SET_BG_COLOR_DARK_GREEN;
                } else if (isValidMove[counter] == 2){
                    boardColor1 = SET_BG_COLOR_BLUE;
                    boardColor2 = SET_BG_COLOR_BLUE;
                } else {
                    boardColor1 = SET_BG_COLOR_LIGHT_GREY;
                    boardColor2 = SET_BG_COLOR_DARK_GREY;
                }
                counter++;
                if (counter % 2 == abs(evenOrOdd-directionIndicator)) {
                    out.print(boardColor1);
                    if (colorArray[counter - 1] == 0) {
                        out.print(teamText2);
                    } else {
                        out.print(teamText1);
                    }
                    out.print(curPiece);
                } else {
                    out.print(boardColor2);
                    if (colorArray[counter - 1] == 0) {
                        out.print(teamText2);
                    } else {
                        out.print(teamText1);
                    }
                    out.print(curPiece);
                }
            }
            outerBoarderSet(out);
            out.print(" " + sideNum + " ");
            blankLine(out);
        }

    }

    private static void blankLine(PrintStream out) {
        out.print(blank);
        out.print("\n");
    }

    private static void outerBoarderSet(PrintStream out) {
        out.print(outerBoarderColor);
        out.print(outerBoarderTextColor);
    }

    private static ChessBoard flipBoard(ChessBoard inBoard){
        ChessBoard outBoard = new ChessBoard();
        for (int i=0; i<=7; i++){
            for (int j=0; j<=7; j++){
                ChessPosition newPosition = new ChessPosition(i+1,j+1);
                ChessPiece copyPiece = inBoard.getPiece(new ChessPosition(8-i,8-j));
                outBoard.addPiece(newPosition, copyPiece);
            }
        }
        return outBoard;
    }

    private static int[][] flipArray(int[][] inArray){
        int[][] outArray = new int[8][8];
        for (int i=0; i<=7; i++){
            for (int j=0; j<=7; j++){
                outArray[7-i][7-j] = inArray[i][j];
            }
        }
        return  outArray;
    }

    public static void main(String[] args) {
        chessBoardToTerminal();

        ChessGame newGame = new ChessGame();

        ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);

        ChessPosition whitekingSetup = new ChessPosition(1,5);
        ChessPosition whitePawnSetup = new ChessPosition(2,2);
        ChessPosition blackRookSetup = new ChessPosition(8,1);
        ChessPosition blackQueenSetup = new ChessPosition(8,4);

        ChessBoard testBoard = new ChessBoard();

        testBoard.addPiece(whitekingSetup,whiteKing);
        testBoard.addPiece(whitePawnSetup,whitePawn);
        testBoard.addPiece(blackRookSetup,blackRook);
        testBoard.addPiece(blackQueenSetup,blackQueen);

        newGame.setBoard(testBoard);

        int[] pos = {0,4};
        printCurBoard(newGame,"WHITE", pos);
        printCurBoard(newGame, "Black",pos);


    }

    }
