package edu.virginia.sde.controller;

import edu.virginia.sde.managers.SessionManager;
import edu.virginia.sde.model.Review;
import edu.virginia.sde.service.ReviewService;
import edu.virginia.sde.service.ReviewServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class MyReviewsController {

    @FXML
    private TableView<Review> reviewsTable;

    @FXML
    private TableColumn<Review, String> courseColumn;

    @FXML
    private TableColumn<Review, Integer> ratingColumn;

    @FXML
    private TableColumn<Review, String> commentColumn;

    private ReviewService reviewService = new ReviewServiceImpl();

    @FXML
    public void initialize() {
        // Set up columns
        courseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseTitle()));
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().ratingProperty().asObject());
        commentColumn.setCellValueFactory(cellData -> cellData.getValue().commentProperty());

        // Add actions column for "Edit" and "Delete"
        TableColumn<Review, Void> actionsColumn = new TableColumn<>("Actions");
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
                    HBox buttons = new HBox(5, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });

        reviewsTable.getColumns().add(actionsColumn);

        // Load reviews
        refreshReviews();
    }

    private void refreshReviews() {
        String username = SessionManager.getUsername();
        System.out.println("[DEBUG] Refreshing reviews for username: " + username);

        if (username != null && !username.isEmpty()) {
            List<Review> reviews = reviewService.getReviewsByUser(username);
            if (reviews.isEmpty()) {
                System.out.println("[DEBUG] No reviews found for username: " + username);
            } else {
                reviews.forEach(review -> System.out.println("[DEBUG] Fetched review: " + review));
            }

            reviewsTable.setItems(FXCollections.observableArrayList(reviews));
        } else {
            showAlert("Error", "No username found. Please log in again.");
        }
    }

    private void handleEdit(Review review) {
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




    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/virginia/sde/reviews/CourseSearchView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) reviewsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Course Search");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to navigate back to the Course Search screen.");
        }
    }

    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
}
