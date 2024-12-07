package edu.virginia.sde.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.");
            return;
        }
        if (username.equals("admin") && password.equals("password")) {
            showAlert("Success", "Login successful!");
        } else {
            showAlert("Error", "Invalid username or password.");
        }
    }

    @FXML
    private void handleCreateUser(ActionEvent event) {
        String password = passwordField.getText();

        // Validate the password length
        if (!isValidPassword(password)) {
            showAlert("Error", "Password must be at least 8 characters long.");
            return;
        }

        // Proceed to open the registration window if the password is valid
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/virginia/sde/views/RegistrationView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Create User");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the registration window.");
        }
    }

    // Utility method to validate the password
    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }



    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
