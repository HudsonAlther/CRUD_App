package edu.virginia.sde.controller;

import edu.virginia.sde.database.DatabaseInitializer;
import edu.virginia.sde.managers.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    // Handle login button action
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        SessionManager.setUsername(username);

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.");
            return;
        }

        if (isValidLogin(username, password)) {
            showAlert("Success", "Login successful!");
            navigateToCourseSearch(event, username);

        } else {
            showAlert("Error", "Invalid username or password.");
        }
    }

    private void navigateToCourseSearch(ActionEvent event, String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/virginia/sde/reviews/CourseSearchView.fxml"));
            Parent root = loader.load();
            CourseSearchController controller = loader.getController();
            controller.setUsername(username);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Course Search");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Course Search screen.");
        }
    }    private boolean isValidLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // If there's a result, the login is valid

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error: " + e.getMessage());
            return false;
        }
    }

    // Handle create user button action
    @FXML
    private void handleCreateUser(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.");
            return;
        }

        if (password.length() < 8) {
            showAlert("Error", "Password must be at least 8 characters long.");
            return;
        }

        if (createUser(username, password)) {
            showAlert("Success", "User created successfully! Please log in with your new credentials.");
            clearFields();
        } else {
            showAlert("Error", "Username already exists or registration failed.");
        }
    }

    // Create a new user in the database
    private boolean createUser(String username, String password) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = DatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error: " + e.getMessage());
            return false;
        }
    }

    // Utility method to clear fields
    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }

    // Utility method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Handle close button action
    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
