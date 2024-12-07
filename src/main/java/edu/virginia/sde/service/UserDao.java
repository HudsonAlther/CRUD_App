package edu.virginia.sde.service;


public interface UserDao {
    boolean addUser(String username, String password);
    boolean userExists(String username);
}

