package edu.virginia.sde.managers;

public class SessionManager {
    private static String username;

    public static void setUsername(String user) {
        username = user;
    }

    public static String getUsername() {
        return username;
    }
}

