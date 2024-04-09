package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Objects;

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

    private static final String boardColor1 = SET_BG_COLOR_LIGHT_GREY;
    private static final String boardColor2 = SET_BG_COLOR_DARK_GREY;
    private static final String teamText1 = SET_TEXT_COLOR_BLACK;
    private static final String teamText2 = SET_TEXT_COLOR_WHITE;
    private static final String outerBoarderColor = SET_BG_COLOR_WHITE;
    private static final String outerBoarderTextColor = SET_TEXT_COLOR_BLACK;
    private static final String blank = "\u001B[0m";

    private static int directionIndicator = 0;

    public static void ChessBoardToTerminal(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        ChessBoardReverse(out);
        blankLine(out);
        blankLine(out);
        ChessBoardForward(out);
        blankLine(out);

    }

    public static void PrintCurBoard(ChessBoard chessBoard, String teamColor){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        directionIndicator = 1;
        letterHeaders = letterHeadersForward;

        int[] rowNum;
        if (Objects.equals(teamColor, "Black")){
            rowNum = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
            chessBoard = flipBoard(chessBoard);
            directionIndicator = 0;

        }
        else {
            rowNum = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
            letterHeaders = letterHeadersReverse;
        }

        PrintString(out,null,null,"header", null);

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
            PrintString(out,rowToPrint,colorToPrint,"layer",rowNum[i]);
        }
        PrintString(out,null,null,"header", null);
        blankLine(out);
    }

    public static void ChessBoardForward(PrintStream out){
        directionIndicator = 0;
        letterHeaders = letterHeadersForward;
        PrintString(out,null,null,"header", null);
        String[] startingLineArray = {rook, knight, bishop, queen, king, bishop, knight, rook};
        Integer[] colorArrayWhite = {0,0,0,0,0,0,0,0};
        Integer[] colorArrayBlack = {1,1,1,1,1,1,1,1};
        PrintString(out,startingLineArray, colorArrayWhite,"layer", 1);
        String[] startingPawnArray = {pawn, pawn, pawn, pawn, pawn, pawn, pawn, pawn};
        PrintString(out,startingPawnArray,colorArrayWhite,"layer", 2);
        String[] emptyRowArray = {"   ","   ","   ","   ","   ","   ","   ","   "};
        PrintString(out,emptyRowArray,colorArrayWhite,"layer", 3);
        PrintString(out,emptyRowArray,colorArrayWhite,"layer", 4);
        PrintString(out,emptyRowArray,colorArrayBlack,"layer", 5);
        PrintString(out,emptyRowArray,colorArrayBlack,"layer", 6);
        PrintString(out,startingPawnArray,colorArrayBlack,"layer", 7);
        PrintString(out,startingLineArray,colorArrayBlack,"layer", 8);
        PrintString(out,null,null,"header", null);
    }

    public static void ChessBoardReverse(PrintStream out){
        directionIndicator = 1;
        letterHeaders = letterHeadersReverse;
        PrintString(out,null,null,"header", null);
        String[] startingLineArray = {rook, knight, bishop, king, queen, bishop, knight, rook};
        Integer[] colorArrayWhite = {0,0,0,0,0,0,0,0};
        Integer[] colorArrayBlack = {1,1,1,1,1,1,1,1};
        PrintString(out,startingLineArray, colorArrayBlack,"layer", 8);
        String[] startingPawnArray = {pawn, pawn, pawn, pawn, pawn, pawn, pawn, pawn};
        PrintString(out,startingPawnArray,colorArrayBlack,"layer", 7);
        String[] emptyRowArray = {"   ","   ","   ","   ","   ","   ","   ","   "};
        PrintString(out,emptyRowArray,colorArrayBlack,"layer", 6);
        PrintString(out,emptyRowArray,colorArrayBlack,"layer", 5);
        PrintString(out,emptyRowArray,colorArrayWhite,"layer", 4);
        PrintString(out,emptyRowArray,colorArrayWhite,"layer", 3);
        PrintString(out,startingPawnArray,colorArrayWhite,"layer", 2);
        PrintString(out,startingLineArray,colorArrayWhite,"layer", 1);
        PrintString(out,null,null,"header", null);
    }

    private static void PrintString(PrintStream out, String[] toPrint, Integer[] colorArray, String layer, Integer sideNum){
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

    public static void main(String[] args) {
//        ChessBoardToTerminal();

        ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);

        ChessPosition whitekingSetup = new ChessPosition(8,4);
        ChessPosition whitePawnSetup = new ChessPosition(7,1);
        ChessPosition blackRookSetup = new ChessPosition(1,1);
        ChessPosition blackQueenSetup = new ChessPosition(1,5);

        ChessBoard testBoard = new ChessBoard();

        testBoard.addPiece(whitekingSetup,whiteKing);
        testBoard.addPiece(whitePawnSetup,whitePawn);
        testBoard.addPiece(blackRookSetup,blackRook);
        testBoard.addPiece(blackQueenSetup,blackQueen);

        PrintCurBoard(testBoard,"White");
        PrintCurBoard(testBoard, "Black");


    }

    }
