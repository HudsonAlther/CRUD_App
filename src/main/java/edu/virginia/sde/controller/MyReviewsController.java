package edu.virginia.sde.controller;

import edu.virginia.sde.model.Review;
import edu.virginia.sde.service.ReviewService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    private String username;
    private ReviewService reviewService;

    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    public void setUsername(String username) {
        this.username = username;
        updateReviewsTable(reviewService.getReviewsByUser(username));
    }

    @FXML
    public void initialize() {
        courseColumn.setCellValueFactory(cellData -> cellData.getValue().courseProperty());
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().ratingProperty().asObject());
        commentColumn.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
    }

    private void updateReviewsTable(List<Review> reviews) {
        ObservableList<Review> reviewList = FXCollections.observableArrayList(reviews);
        reviewsTable.setItems(reviewList);
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            Parent courseSearchRoot = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/view/CourseSearchView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(courseSearchRoot));
            stage.setTitle("Course Search");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Course Search screen.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
