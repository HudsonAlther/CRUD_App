package edu.virginia.sde.controller;
import edu.virginia.sde.service.UserService;
import edu.virginia.sde.service.UserServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorMessage;

    private final UserService userService = new UserServiceImpl();

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorMessage.setText("Username and password cannot be empty.");
            return;
        }

        boolean isValidUser = userService.validateUser(username, password);
        if (isValidUser) {
            errorMessage.setText("Login successful!");
            // TODO: Transition to the next scene
        } else {
            errorMessage.setText("Invalid username or password.");
        }
    }
}
