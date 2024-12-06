package edu.virginia.sde.reviews;

import edu.virginia.sde.managers.SessionManager;
import edu.virginia.sde.model.Course;
import edu.virginia.sde.model.Review;
import edu.virginia.sde.service.ReviewService;
import edu.virginia.sde.service.ReviewServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class CourseReviewsController {

    @FXML private Label titleLabel;
    @FXML private TableView<Review> reviewsTable;
    @FXML private TableColumn<Review, Integer> ratingColumn;
    @FXML private TableColumn<Review, String> commentColumn;
    @FXML private TableColumn<Review, String> timestampColumn;
    @FXML private Label averageRatingLabel;

    @FXML private TextField ratingInput;
    @FXML private TextField commentInput;

    private ObservableList<Review> reviews = FXCollections.observableArrayList();
    private final String currentUserId = SessionManager.getUsername();
    private final ReviewService reviewService = new ReviewServiceImpl();

    @FXML
    public void initialize() {
        // Initialize table columns
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        updateAverageRating();
    }

    public void setCourse(Course course) {
        // Load reviews from the service
        reviews = FXCollections.observableArrayList(reviewService.getReviewsByCourseId(course.getId()));
        reviewsTable.setItems(reviews);

        String courseNumberStr = String.valueOf(course.getNumber());
        setCourseDetails(course.getSubject(), courseNumberStr, course.getTitle());
        updateAverageRating();
    }

    @FXML
    private void handleSubmitReview() {
        try {
            int rating = Integer.parseInt(ratingInput.getText());
            if (rating < 1 || rating > 5) {
                showAlert("Invalid Rating", "Rating must be between 1 and 5.");
                return;
            }

            String comment = commentInput.getText();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            Review newReview = new Review(0, currentUserId, 0, rating, comment, timestamp.toString());

            // Remove existing review by the user
            reviews.removeIf(review -> review.getUsername().equals(currentUserId));

            // Add new review
            reviews.add(newReview);

            reviewsTable.setItems(reviews);
            updateAverageRating();
            clearInputFields();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for the rating.");
        }
    }

    @FXML
    private void handleDeleteReview() {
        reviews.removeIf(review -> review.getUsername().equals(currentUserId));
        reviewsTable.setItems(reviews);
        updateAverageRating();
    }

    @FXML
    private void handleBack(ActionEvent event) {
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

    private void updateAverageRating() {
        double average = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        averageRatingLabel.setText(String.format("Average Rating: %.2f", average));
    }

    private void setCourseDetails(String mnemonic, String number, String title) {
        titleLabel.setText(String.format("Course Reviews for %s %s: %s", mnemonic, number, title));
    }

    private void clearInputFields() {
        ratingInput.clear();
        commentInput.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
