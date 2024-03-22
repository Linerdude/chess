package ConsoleClient;

import ServerFacade.ServerFacade;

public class Console {
    private final ServerFacade server = new ServerFacade("http://localhost:8080");
    private boolean userAuthorized;

    private void printLoggedOutMenu() {
        String printString = String.format("""
                
                %s OPTIONS:
                    register <username> <password> <email> - creates an account and logs user in
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
}
