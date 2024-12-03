package edu.virginia.sde.service;

import edu.virginia.sde.model.User;

import java.sql.*;

public class UserServiceImpl implements UserService {
    private static final String DB_URL = "jdbc:sqlite:course_reviews.db";

    @Override
    public boolean createUser(User user) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getUser(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getString("username"), resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean validateUser(String username, String password) {
        User user = getUser(username);
        return user != null && user.getPassword().equals(password);
    }
}
