package edu.virginia.sde.controller;
import edu.virginia.sde.model.User;
import edu.virginia.sde.service.UserService;
import edu.virginia.sde.service.UserServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    protected TextField usernameField;

    @FXML
    protected PasswordField passwordField;

    @FXML
    private VBox registerForm;

    protected UserService userService = new UserServiceImpl();

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username or password cannot be empty.");
            return;
        }

        if (userService.validateUser(username, password)) {
            // Load the next scene
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/virginia/sde/reviews/CourseSearchView.fxml"));
                Parent root = loader.load();
                CourseSearchController courseSearchController = loader.getController();
                courseSearchController.setUsername(username);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Course Search");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load the Course Search screen.");
            }
        } else {
            showAlert("Error", "Invalid username or password.");
        }
    }



    @FXML
    public void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and password cannot be empty.");
            return;
        }

        if (password.length() < 8) {
            showAlert("Error", "Password must be at least 8 characters long.");
            return;
        }

        User newUser = new User(username, password);
        boolean isCreated = userService.createUser(newUser);

        if (isCreated) {
            showAlert("Success", "User registered successfully! Please log in.");
        } else {
            showAlert("Error", "Username already exists. Please choose another.");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setUsernameField(TextField usernameField) {
        this.usernameField = usernameField;
    }

    public void setPasswordField(PasswordField passwordField) {
        this.passwordField = passwordField;
    }




}
