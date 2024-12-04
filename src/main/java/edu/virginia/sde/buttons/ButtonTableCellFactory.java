package edu.virginia.sde.buttons;

import edu.virginia.sde.model.Review;
import edu.virginia.sde.service.ReviewService;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class ButtonTableCellFactory extends TableCell<Review, Void> {

    private final Button editButton = new Button("Edit");
    private final Button deleteButton = new Button("Delete");

    public ButtonTableCellFactory(ReviewService reviewService, String username, Runnable refreshCallback) {
        editButton.setOnAction(event -> {
            Review review = getTableView().getItems().get(getIndex());
            handleEdit(review, reviewService, username, refreshCallback);
        });

        deleteButton.setOnAction(event -> {
            Review review = getTableView().getItems().get(getIndex());
            handleDelete(review, reviewService, username, refreshCallback);
        });
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

    private void handleEdit(Review review, ReviewService reviewService, String username, Runnable refreshCallback) {
        TextInputDialog dialog = new TextInputDialog(review.getComment());
        dialog.setTitle("Edit Review");
        dialog.setHeaderText("Edit your review for " + review.getCourseId());
        dialog.setContentText("New Comment:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newComment -> {
            review.setComment(newComment);
            if (reviewService.updateReview(review)) {
                System.out.println("[INFO] Review updated successfully.");
                refreshCallback.run(); // Refresh the table
            } else {
                System.err.println("[ERROR] Failed to update review.");
            }
        });
    }


    private void handleDelete(Review review, ReviewService reviewService, String username, Runnable refreshCallback) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this review?");
        confirm.setHeaderText("Delete Review");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (reviewService.deleteReview(review.getReviewId(), username)) {
                System.out.println("[INFO] Review deleted successfully.");
                refreshCallback.run();
            } else {
                System.err.println("[ERROR] Failed to delete review.");
            }
        }
    }
}
