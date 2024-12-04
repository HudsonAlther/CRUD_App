package edu.virginia.sde.service;
import edu.virginia.sde.model.User;

public interface UserService {
    User getUser(String username);
    boolean validateUser(String username, String password);
    boolean createUser(String username, String password);


}