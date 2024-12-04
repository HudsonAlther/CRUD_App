package edu.virginia.sde.controller;

import edu.virginia.sde.model.Review;
import edu.virginia.sde.model.Course;
import edu.virginia.sde.service.ReviewService;
import edu.virginia.sde.service.ReviewServiceImpl;
import edu.virginia.sde.service.CourseService;
import edu.virginia.sde.service.CourseServiceImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        courseComboBox.setItems(FXCollections.observableArrayList(courseService.getAllCourses()));
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

            // Extract course id and title for the review
            String courseSubject = selectedCourse.getSubject();
            int courseNumber = selectedCourse.getNumber();
            int courseId = selectedCourse.getNumber();
            String courseTitle = selectedCourse.getTitle();

            System.out.println("Submitting review for course: " + courseSubject + " " + courseNumber + " with rating: " + rating);

            Review review = new Review(0, username, courseId, courseTitle , rating, comment);
            boolean success = reviewService.addReview(review);

            if (success) {
                showAlert("Success", "Review submitted successfully!");
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
}
