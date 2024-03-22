package ConsoleClient;

import ServerFacade.ServerFacade;
import chess.ChessGame;
import chess.ChessPiece;
import exception.ResponseException;
import requestRecords.CreateGameRequest;
import requestRecords.JoinGameRequest;
import requestRecords.LoginRequest;
import requestRecords.RegisterRequest;
import responseRecords.*;
import ui.DrawChessBoard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Console {
    private final ServerFacade server = new ServerFacade("http://localhost:8080");
    private boolean userAuthorized;
    private String userAuthToken;
    private boolean running = true;
    private boolean printLoggedOutMenu = true;
    private boolean printLoggedInMenu = false;

    ArrayList<String> validCommands = new ArrayList<>(Arrays.asList("register", "login", "list", "create",
            "join", "observe", "logout", "quit", "help"));

    public void run() {
        while (this.running) {
            if (this.printLoggedOutMenu) {
                printLoggedOutMenu();
                this.printLoggedOutMenu = false;
            } else if (this.printLoggedInMenu) {
                printLoggedInMenu();
                this.printLoggedInMenu = false;
            }

            ArrayList<String> userInput = new ArrayList<>();
            try {
                userInput = (ArrayList<String>) promptInput();
            } catch (IOException ex) { System.out.print("An error occurred. Please try again"); }

            parseCommands(userInput);
        }
    }

    private void parseCommands(ArrayList<String> userInput){
        String unrecognizedCommandString = "Command not recognized. Type help to list available commands.\n";
        assert !userInput.isEmpty();
        String firstCommand = userInput.getFirst().toLowerCase();
        if (firstCommand.isEmpty()) {
            return;
        }
        else if (!validCommands.contains(firstCommand)) {
            System.out.print(unrecognizedCommandString);
            return;
        }
        ArrayList<String> userArgs;
        userArgs = userInput;
        userArgs.removeFirst();
        ArrayList<String> validate;
        try {
            boolean invalidInput = false;
            if (!userAuthorized) {
                switch (firstCommand) {
                    case "register" -> {
                        validate = new ArrayList<>(Arrays.asList("str", "str", "str"));
                        if (isValidInput(userArgs, validate)) { register(userArgs);
                        } else { invalidInput = true; }
                    }
                    case "login" -> {
                        validate = new ArrayList<>(Arrays.asList("str", "str"));
                        if (isValidInput(userArgs, validate)) { login(userArgs);
                        } else { invalidInput = true; }
                    }
                }
            } else {
                switch (firstCommand) {
                    case "list" -> {
                        if (userArgs.isEmpty()) { list();
                        } else { invalidInput = true; }
                    }
                    case "create" -> {
                        validate = new ArrayList<>(List.of("str"));
                        if (isValidInput(userArgs, validate)) { create(userArgs);
                        } else { invalidInput = true; }
                    }
                    case "join" -> {
                        validate = new ArrayList<>(Arrays.asList("int", "str"));
                        if (isValidInput(userArgs, validate)) { join(userArgs);
                        } else { invalidInput = true; }
                    }
                    case "observe" -> {
                        validate = new ArrayList<>(List.of("int"));
                        if (isValidInput(userArgs, validate)) { observe(userArgs);
                        } else { invalidInput = true; }
                    }
                    case "logout" -> {
                        if (userArgs.isEmpty()) { logout();
                        } else { invalidInput = true; }
                    }
                }
            }
            // Always-available options
            switch (firstCommand) {
                case "quit" -> {
                    if (userArgs.isEmpty()) { quit();
                    } else { invalidInput = true; }
                }
                case "help" -> {
                    if (userArgs.isEmpty()) { help();
                    } else { invalidInput = true; }
                }
            }
            if (invalidInput) {
                System.out.print("Invalid command input. Type help and format your command according to the menu.\n");
            }
        } catch (ResponseException ex) {
            System.out.print("An error occurred while communicating with the server: " + ex.getMessage() + "\n");
        }
    }

    private boolean isValidInput(ArrayList<String> userInput, ArrayList<String> validTypes) {
        if (userInput.size() != validTypes.size()) { return false; }
        boolean isValidInput = true;
        for (int i = 0; i < validTypes.size(); i++) {
            String input = userInput.get(i);
            String type = validTypes.get(i);
            switch(type) {
                case "str" -> isValidInput = isValidInput && !isNumeric(input) && !input.isEmpty();
                case "int" -> isValidInput = isValidInput && isNumeric(input);
            }
        }
        return isValidInput;
    }
    private void printLoggedOutMenu() {
        String printString = String.format("""
                
                %s OPTIONS:
                    register <username> <password> <email> - creates an account and logs in
                    login <username> <password> - logs user in
                    quit - quits program
                    help - lists available commands
                
                """, getUserAuthStatusAsString(this.userAuthorized));
        System.out.print(printString);
    }

    private void printLoggedInMenu() {
        String printString = String.format("""
            
            %s OPTIONS:
                list - lists available games
                create <name> - creates a game
                join <ID> <WHITE|BLACK> - joins a specified game as the chosen color
                observe <ID> - joins a specified game as an observer
                logout - logs you out
                quit - quits program
                help - lists available commands
            
            """, getUserAuthStatusAsString(this.userAuthorized));
        System.out.print(printString);
    }

    private String getUserAuthStatusAsString(boolean userAuthorized) {
        if (userAuthorized) { return "LOGGED_IN"; }
        else { return "LOGGED_OUT"; }
    }

    private Collection<String> promptInput() throws IOException {
        System.out.printf("[%s] >> ", getUserAuthStatusAsString(this.userAuthorized));
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return new ArrayList<>(List.of(reader.readLine().split(" ")));
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.valueOf(str);
            return true;
        } catch (NumberFormatException ex) { return false; }
    }

    private void setAuthorization(String authToken) {
        this.userAuthToken = authToken;
        this.userAuthorized = (authToken != null);
        if (this.userAuthorized) { this.printLoggedInMenu = true;
        } else { this.printLoggedOutMenu = true; }
    }

    private void register(ArrayList<String> userArgs) throws ResponseException {
        RegisterResponse rResponse = server.register(
                new RegisterRequest(userArgs.get(0), userArgs.get(1), userArgs.get(2)));
        System.out.print("Registered user " + rResponse.username() + ".\n");
        login(new ArrayList<>(Arrays.asList(userArgs.get(0), userArgs.get(1))));
    }

    private void login(ArrayList<String> userArgs) throws ResponseException {
        LoginResponse lResponse = server.login(
                new LoginRequest(userArgs.get(0), userArgs.get(1)));
        setAuthorization(lResponse.authToken());
        System.out.print("Logged in user " + lResponse.username() + ".\n");
    }

    private void list() throws ResponseException {
        ListGamesResponse response = server.listGames();
        System.out.print("\nCURRENT GAMES:\n");
        System.out.print("    Game Name | Game ID | White Player Username | Black Player Username\n");
        for(ListGameInfo gameInfo: response.games()) {
            System.out.print("    " + gameInfo.gameName() + " | " +
                    gameInfo.gameID() + " | " +
                    gameInfo.whiteUsername() + " | " +
                    gameInfo.blackUsername() + "\n");
        }
        System.out.print("\n");
    }

    private void create(ArrayList<String> userArgs) throws ResponseException {
        CreateGameResponse response = server.createGame(new CreateGameRequest(userArgs.getFirst()), this.userAuthToken);
        System.out.print("New game \"" + userArgs.getFirst() + "\" created with ID: " + response.gameID() + "\n");
    }

    private void join(ArrayList<String> userArgs) throws ResponseException {
        String color;
        if (userArgs.get(1) != null) { color = userArgs.get(1).toUpperCase();
        } else { color = null; }
        int gameID = Integer.parseInt(userArgs.get(0));
        ChessGame.TeamColor colorObj = null;
        if (Objects.equals(color, "WHITE")){
            colorObj = ChessGame.TeamColor.WHITE;
        } else if (Objects.equals(color, "BLACK")){
            colorObj = ChessGame.TeamColor.BLACK;
        } else {
            System.out.print("Invalid color selection.");
        }
        server.joinGame(new JoinGameRequest(colorObj, gameID), this.userAuthToken);
        // Default board printing for phase 5
        //     Actual implementation will be done via websockets in phase 6
        System.out.print("GameID: " + gameID + "\n");
        DrawChessBoard.ChessBoardToTerminal();
    }

    private void observe(ArrayList<String> userArgs) throws ResponseException {
        join(new ArrayList<>(Arrays.asList(userArgs.getFirst(), null)));
    }

    private void logout() throws ResponseException {
        server.logout(this.userAuthToken);
        setAuthorization(null);
        System.out.print("Logged out.\n");
    }

    private void quit() throws ResponseException {
        if (this.userAuthorized) { logout(); }
        System.out.print("Quitting...\n\n");
        this.running = false;
    }

    private void help() {
        if (!this.userAuthorized) { printLoggedOutMenu();
        } else { printLoggedInMenu(); }
    }
}
