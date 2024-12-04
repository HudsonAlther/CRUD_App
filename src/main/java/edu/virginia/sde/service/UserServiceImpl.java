package edu.virginia.sde.service;

import edu.virginia.sde.model.User;

import java.sql.*;

public class UserServiceImpl implements UserService {

    private static final String DB_URL = "jdbc:sqlite:course_reviews.db";

    public UserServiceImpl() {
        initializeDatabase();
    }

    // Ensures the Users table exists
    private void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," + // Added AUTOINCREMENT for automatic id generation
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL" +
                ")";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean createUser(String username, String password) {
        String insertQuery = "INSERT INTO Users (username, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            // Insert new user
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.executeUpdate();
            return true; // User created successfully

        } catch (SQLException e) {
            System.err.println("SQL Error during createUser: " + e.getMessage()); // Added more detailed logging
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getUser(String username) {
        String query = "SELECT * FROM Users WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return new User(rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // User not found
    }

    @Override
    public boolean validateUser(String username, String password) {
        User user = getUser(username);
        return user != null && user.getPassword().equals(password);
    }
}
