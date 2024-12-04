package edu.virginia.sde.controller;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
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


    private String username;
    private ReviewService reviewService;

    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    public void setUsername(String username) {
        this.username = username;
        if (reviewService != null) {
            List<Review> reviews = reviewService.getReviewsByUser(username);
            updateReviewsTable(reviews != null ? reviews : new ArrayList<>());
        } else {
            showAlert("Error", "ReviewService is not initialized.");
        }
    }

    @FXML
    public void initialize() {
        courseColumn.setCellValueFactory(cellData -> cellData.getValue().courseProperty());
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().ratingProperty().asObject());
        commentColumn.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
        reviewsTable.setPlaceholder(new Label("No reviews available."));
    }

    private void updateReviewsTable(List<Review> reviews) {
        ObservableList<Review> reviewList = FXCollections.observableArrayList(reviews);
        reviewsTable.setItems(reviewList);
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/virginia/sde/reviews/CourseSearchView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Course Search");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to navigate back to the Course Search screen. Please try again.");
        }
    }

    @FXML
    public void handleMyReviews(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/virginia/sde/reviews/MyReviewsView.fxml"));
            Parent root = loader.load();

            MyReviewsController myReviewsController = loader.getController();
            myReviewsController.setReviewService(new ReviewServiceImpl()); // Set the ReviewService
            myReviewsController.setUsername(username); // Set the username after initializing ReviewService

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("My Reviews");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the My Reviews screen.");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
