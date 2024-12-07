package edu.virginia.sde.controller;

import edu.virginia.sde.managers.SessionManager;
import edu.virginia.sde.model.Review;
import edu.virginia.sde.model.Course;
import edu.virginia.sde.reviews.CourseReviewsController;
import edu.virginia.sde.service.ReviewService;
import edu.virginia.sde.service.ReviewServiceImpl;
import edu.virginia.sde.service.CourseService;
import edu.virginia.sde.service.CourseServiceImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import javax.security.auth.Refreshable;
import java.io.IOException;

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

    private CourseReviewsController courseReviewsController;


    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    public void initialize() {
        try {
            var courses = courseService.getAllCourses();
            if (courses == null || courses.isEmpty()) {
                showAlert("Error", "No courses available to review.");
                return;
            }
            courseComboBox.setItems(FXCollections.observableArrayList(courses));

            ratingField.setTextFormatter(new TextFormatter<>(change -> {
                if (change.getControlNewText().matches("[0-9]{0,1}")) {
                    return change;
                }
                return null;
            }));
        } catch (Exception e) {
            showAlert("Error", "Failed to load courses.");
            e.printStackTrace();
        }
    }



    @FXML
    public void handleSubmitReview() {
        Course selectedCourse = courseComboBox.getSelectionModel().getSelectedItem();
        String ratingText = ratingField.getText();
        String comment = commentField.getText();

        if (selectedCourse == null || ratingText.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        String username = SessionManager.getUsername();
        if (username == null || username.isEmpty()) {
            showAlert("Error", "You must be logged in to submit a review.");
            return;
        }

        try {
            int rating = Integer.parseInt(ratingText);
            if (rating < 1 || rating > 5) {
                showAlert("Error", "Rating must be between 1 and 5.");
                return;
            }

            Review review = new Review(0, username, selectedCourse.getId(), rating, comment, selectedCourse.getTitle());
            boolean success = reviewService.addReview(review);

            if (success) {
                showAlert("Success", "Review submitted successfully!");
                ratingField.clear();
                commentField.clear();

                if (courseReviewsController != null) {
                    courseReviewsController.refreshReviews();
                }

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
    private boolean cameFromCourseReviews = false;

    public void setCourseReviewsController(CourseReviewsController controller) {
        this.courseReviewsController = controller;
        this.cameFromCourseReviews = true;
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
            selectedCourse.refreshAverageRating();
            courseComboBox.setItems(FXCollections.observableArrayList(courseService.getAllCourses()));
            courseComboBox.getSelectionModel().select(selectedCourse);
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        if (cameFromCourseReviews) {
            closeWindow();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/virginia/sde/reviews/CourseSearchView.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Course Search");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to navigate back to the Course Search screen.");
            }
        }
    }



}
