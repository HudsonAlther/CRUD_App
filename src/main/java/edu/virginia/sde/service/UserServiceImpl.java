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
                "id INTEGER PRIMARY KEY," +
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
    public boolean createUser(User user) {
        String checkQuery = "SELECT COUNT(*) FROM Users WHERE username = ?";
        String insertQuery = "INSERT INTO Users (username, password) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

            // Check if username already exists
            checkStmt.setString(1, user.getUsername());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Username exists
            }

            // Insert new user
            insertStmt.setString(1, user.getUsername());
            insertStmt.setString(2, user.getPassword());
            return insertStmt.executeUpdate() > 0; // Returns true if insertion is successful
        } catch (SQLException e) {
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
