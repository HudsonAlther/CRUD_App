package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CourseReviewsController {
    @FXML
    private Label messageLabel;
    @FXML
    private Button actionButton;

    @FXML
    public void handleButton(ActionEvent event) {
        messageLabel.setText("Button pressed!");
    }
}