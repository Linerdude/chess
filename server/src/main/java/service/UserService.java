package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import requestRecords.RegisterRequest;
import requestRecords.LoginRequest;
import responseRecords.RegisterResponse;
import responseRecords.LoginResponse;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class UserService {
    private final MemoryDataAccess dataAccess;

    public UserService() {
        this.dataAccess = new MemoryDataAccess();
    }

    public RegisterResponse register(RegisterRequest rRequest) throws DataAccessException {
        ArrayList<UserData> cur_users = (ArrayList<UserData>) dataAccess.listUser();
        UserData user = dataAccess.getUser(rRequest.username());
        if (cur_users.contains(user)){
            throw new DataAccessException("Error: already taken");
        } else {
            UserData new_user = new UserData(rRequest.username(),rRequest.password(),rRequest.email());
            dataAccess.addUser(new_user);
            String newToken = createAuth();
            AuthData newAuth = new AuthData(newToken,user.username());
            dataAccess.addAuth(newAuth);
            return new RegisterResponse(rRequest.username(),newToken);
        }
    }
    public LoginResponse login(LoginRequest lRequest) throws DataAccessException {
        ArrayList<UserData> cur_users = (ArrayList<UserData>) dataAccess.listUser();
        UserData user = dataAccess.getUser(lRequest.username());
        if (user != null) {
            if (cur_users.contains(user)) {
                String newToken = createAuth();
                AuthData newAuth = new AuthData(newToken, user.username());
                dataAccess.addAuth(newAuth);
                return new LoginResponse(lRequest.username(), newToken);
            } else {
                throw new DataAccessException("Error: unauthorized");
            }
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }
    public void logout(String authToken) throws DataAccessException {
        if (dataAccess.getAuth(authToken) != null) {
            dataAccess.deleteAuth(authToken);
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public String createAuth(){
        return UUID.randomUUID().toString();
    }

}
