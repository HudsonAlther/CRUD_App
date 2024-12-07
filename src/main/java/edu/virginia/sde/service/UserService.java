package edu.virginia.sde.service;
import edu.virginia.sde.model.User;

import java.sql.SQLException;

public interface UserService {
    User getUser(String username);
    boolean validateUser(String username, String password);
    boolean createUser(String username, String password);

    boolean registerUser(String username, String password) throws SQLException;
    boolean isValidPassword(String password);


}