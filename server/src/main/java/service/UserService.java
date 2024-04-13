package service;

import dataAccess.DataAccessException;
import dataAccess.SQLDataAccess;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import requestRecords.RegisterRequest;
import requestRecords.LoginRequest;
import responseRecords.RegisterResponse;
import responseRecords.LoginResponse;

import java.util.UUID;

public class UserService {
//    private final MemoryDataAccess dataAccess = new MemoryDataAccess();
    private final SQLDataAccess dataAccess = new SQLDataAccess();

    public UserService() throws DataAccessException {
    }


    public RegisterResponse register(RegisterRequest rRequest) throws DataAccessException {
        if ((rRequest.username() == null || rRequest.password() == null || rRequest.email() == null)){
            throw new DataAccessException("Error: bad request");
        }
        UserData user = dataAccess.getUser(rRequest.username());
        if (user != null){
            throw new DataAccessException("Error: already taken");
        } else {
            UserData newUser = new UserData(rRequest.username(),rRequest.password(),rRequest.email());
            dataAccess.addUser(newUser);
            String newToken = createAuth();
            AuthData newAuth = new AuthData(newToken,newUser.username());
            dataAccess.addAuth(newAuth);
            return new RegisterResponse(rRequest.username(),newToken);
        }
    }
    public LoginResponse login(LoginRequest lRequest) throws DataAccessException {
        UserData user = dataAccess.getUser(lRequest.username());
//        System.out.println(user);
        if (user != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(lRequest.password(),user.password())) {
                String newToken = createAuth();
                AuthData newAuth = new AuthData(newToken, user.username());
                dataAccess.addAuth(newAuth);
//                System.out.println(dataAccess.listAuth());
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
            return;
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public String createAuth(){
        return UUID.randomUUID().toString();
    }

}
