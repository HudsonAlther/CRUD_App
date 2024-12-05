package edu.virginia.sde.controller;

import edu.virginia.sde.managers.SessionManager;
import edu.virginia.sde.model.Review;
import edu.virginia.sde.model.Course;
import edu.virginia.sde.service.ReviewService;
import edu.virginia.sde.service.ReviewServiceImpl;
import edu.virginia.sde.service.CourseService;
import edu.virginia.sde.service.CourseServiceImpl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import javax.security.auth.Refreshable;

public class WriteReviewController {

    @FXML
    private ComboBox<Course> courseComboBox;
    @FXML
    private TextField ratingField;
    @FXML
    private TextArea commentField;

    private final ReviewService reviewService = new ReviewServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();

    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    public void initialize() {
        var courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            showAlert("Error", "No courses available to review.");
        }
        courseComboBox.setItems(FXCollections.observableArrayList(courses));

        // Limit ratingField to numeric input only
        ratingField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().matches("[0-9]*")) {
                return change;
            }
            return null;
        }));
    }


    @FXML
    public void handleSubmitReview() {
        Course selectedCourse = courseComboBox.getSelectionModel().getSelectedItem();
        String ratingText = ratingField.getText();
        String comment = commentField.getText();

        if (selectedCourse == null || ratingText.isEmpty() || comment.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        try {
            int rating = Integer.parseInt(ratingText);
            if (rating < 1 || rating > 5) {
                showAlert("Error", "Rating must be between 1 and 5.");
                return;
            }

            Review review = new Review(0, SessionManager.getUsername(), selectedCourse.getId(), rating, comment, selectedCourse.getTitle());
            boolean success = reviewService.addReview(review);

            if (success) {
                showAlert("Success", "Review submitted successfully!");
                refresh();
                closeWindow();
            } else {
                showAlert("Error", "Failed to submit review.");
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "Rating must be a valid number.");
        }
    }


    @FXML
    public void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) courseComboBox.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void refresh() {
        Course selectedCourse = courseComboBox.getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
            // Refresh the course's average rating
            selectedCourse.refreshAverageRating();

            // Update the ComboBox display
            courseComboBox.setItems(FXCollections.observableArrayList(courseService.getAllCourses()));

            // Re-select the updated course
            courseComboBox.getSelectionModel().select(selectedCourse);
        }
    }


}
