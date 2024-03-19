package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import java.util.Objects;

import static ui.EscapeSequences.*;

public class DrawChessBoard {

    static String[] letterHeaders = {"   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "};
    static String[] letterHeadersForward = {"   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "};
    static String[] letterHeadersReverse = {"   ", " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", "   "};

    static String[] numHeaders = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
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

    public static void ChessBoardToTerminal(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        ChessBoardForward(out);
        blankLine(out);
        blankLine(out);
        ChessBoardReverse(out);

    }

    public static void ChessBoardForward(PrintStream out){
        letterHeaders = letterHeadersForward;
        PrintString(out,null,null,"header", null);
        String[] startingLineArray = {rook, knight, bishop, queen, king, bishop, knight, rook};
        int[] colorArrayWhite = {0,0,0,0,0,0,0,0};
        int[] colorArrayBlack = {1,1,1,1,1,1,1,1};
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
        letterHeaders = letterHeadersReverse;
        PrintString(out,null,null,"header", null);
        String[] startingLineArray = {rook, knight, bishop, queen, king, bishop, knight, rook};
        int[] colorArrayWhite = {0,0,0,0,0,0,0,0};
        int[] colorArrayBlack = {1,1,1,1,1,1,1,1};
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

    private static void PrintString(PrintStream out, String[] toPrint, int[] colorArray, String layer, Integer sideNum){
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
                if (counter % 2 == evenOrOdd) {
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
            out.print(" " + sideNum.toString() + " ");
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

    public static void main(String[] args) {
        ChessBoardToTerminal();
    }

    }
