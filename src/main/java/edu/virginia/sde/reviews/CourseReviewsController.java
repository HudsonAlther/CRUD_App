package edu.virginia.sde.reviews;

import edu.virginia.sde.controller.WriteReviewController;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private int courseId;


    @FXML private TextField ratingInput;
    @FXML private TextField commentInput;

    private ObservableList<Review> reviews = FXCollections.observableArrayList();
    private final String currentUserId = SessionManager.getUsername();
    private final ReviewService reviewService = new ReviewServiceImpl();
    @FXML private TableColumn<Review, Void> actionsColumn;

    @FXML
    public void initialize() {
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        addActionsColumn();
        updateAverageRating();
    }


    private void addActionsColumn() {
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> handleEdit(getTableRow().getItem()));
                deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Review review = getTableRow().getItem();
                    if (review != null && review.getUsername().equals(SessionManager.getUsername())) {
                        HBox buttons = new HBox(5, editButton, deleteButton);
                        setGraphic(buttons);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private void handleEdit(Review review) {
        if (!review.getUsername().equals(SessionManager.getUsername())) {
            showAlert("Error", "You can only edit your own reviews.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Review");
        dialog.setHeaderText("Edit your review for " + review.getCourseTitle());

        TextField commentField = new TextField(review.getComment());
        TextField ratingField = new TextField(String.valueOf(review.getRating()));

        VBox vbox = new VBox(10, new Label("Comment:"), commentField, new Label("Rating:"), ratingField);
        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    int newRating = Integer.parseInt(ratingField.getText().trim());
                    String newComment = commentField.getText().trim();

                    if (newRating < 1 || newRating > 5) {
                        showAlert("Error", "Rating must be between 1 and 5.");
                        return;
                    }

                    review.setRating(newRating);
                    review.setComment(newComment);

                    if (reviewService.updateReview(review)) {
                        refreshReviews();
                    } else {
                        showAlert("Error", "Failed to update review.");
                    }
                } catch (NumberFormatException e) {
                    showAlert("Error", "Rating must be a valid integer.");
                }
            }
        });
    }

    private void handleDelete(Review review) {
        if (!review.getUsername().equals(SessionManager.getUsername())) {
            showAlert("Error", "You can only delete your own reviews.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this review?");
        confirm.setHeaderText("Delete Review");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (reviewService.deleteReview(review.getReviewId(), SessionManager.getUsername())) {
                    refreshReviews();
                } else {
                    showAlert("Error", "Failed to delete review.");
                }
            }
        });
    }


    public void refreshReviews() {
        List<Review> reviewsList = reviewService.getReviewsByCourseId(courseId);
        reviews = FXCollections.observableArrayList(reviewsList);
        reviewsTable.setItems(reviews);
        updateAverageRating();
    }





    public void setCourse(Course course) {
        this.courseId = course.getId();
        setCourseDetails(course.getSubject(), String.valueOf(course.getNumber()), course.getTitle());
        refreshReviews();
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
            reviews.removeIf(review -> review.getUsername().equals(currentUserId));
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
        System.out.println("Calculating average rating from the following ratings:");
        reviews.forEach(review -> System.out.println(review.getRating()));

        double average = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        averageRatingLabel.setText(String.format("Average Rating: %.2f", average));
    }


    @FXML
    private void handleWriteReview(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/virginia/sde/reviews/WriteReviewView.fxml"));
            Parent root = loader.load();
            WriteReviewController writeReviewController = loader.getController();
            writeReviewController.setUsername(SessionManager.getUsername());
            writeReviewController.setCourseReviewsController(this);
            Stage stage = new Stage();
            stage.setTitle("Write Review");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the Write Review window.");
        }
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

    @FXML
    private void handleEditReview() {
        Review selectedReview = reviewsTable.getSelectionModel().getSelectedItem();
        if (selectedReview != null) {
            ratingInput.setText(String.valueOf(selectedReview.getRating()));
            commentInput.setText(selectedReview.getComment());
            reviews.remove(selectedReview);
        } else {
            showAlert("No Selection", "Please select a review to edit.");
        }
    }

}
