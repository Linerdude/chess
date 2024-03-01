package service;

import dataAccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class UserService {
    private final MemoryDataAccess dataAccess;

    public UserService() {
        this.dataAccess = new MemoryDataAccess();
    }

    public AuthData register(UserData user) {
        ArrayList<UserData> cur_users = (ArrayList<UserData>) dataAccess.listUser();
        if (cur_users.contains(user)){
            return null;
        } else {
            dataAccess.addUser(user);
            String newToken = createAuth();
            AuthData newAuth = new AuthData(newToken,user.username());
            dataAccess.addAuth(newAuth);
            return newAuth;
        }
    }
    public AuthData login(UserData user) {
        ArrayList<UserData> cur_users = (ArrayList<UserData>) dataAccess.listUser();
        if (cur_users.contains(user)){
            String newToken = createAuth();
            AuthData newAuth = new AuthData(newToken,user.username());
            dataAccess.addAuth(newAuth);
            return newAuth;
        }
        else{
            return null;
        }
    }
    public void logout(UserData user) {
        ArrayList<AuthData> cur_auth = (ArrayList<AuthData>) dataAccess.listAuth();
        String toRemove = null;
        for (AuthData auth : cur_auth){
            if (Objects.equals(auth.username(), user.username())){
                toRemove = auth.authToken();
            }
        }
        dataAccess.deleteAuth(toRemove);
    }

    public String createAuth(){
        return UUID.randomUUID().toString();
    }

}
