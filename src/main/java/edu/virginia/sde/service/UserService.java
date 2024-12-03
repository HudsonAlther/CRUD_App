package edu.virginia.sde.service;
import edu.virginia.sde.model.User;

public interface UserService {
    boolean createUser(User user);
    User getUser(String username);
    boolean validateUser(String username, String password);
}