package edu.virginia.sde.controller;

import edu.virginia.sde.model.Review;
import edu.virginia.sde.service.ReviewService;
import edu.virginia.sde.service.ReviewServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MyReviewsController {

    @FXML
    private TableView<Review> reviewsTable;
    @FXML
    private TableColumn<Review, String> courseColumn;
    @FXML
    private TableColumn<Review, Integer> ratingColumn;
    @FXML
    private TableColumn<Review, String> commentColumn;

    private final ReviewService reviewService = new ReviewServiceImpl();

    @FXML
    public void initialize() {
        courseColumn.setCellValueFactory(cellData -> cellData.getValue().courseProperty());
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().ratingProperty().asObject());
        commentColumn.setCellValueFactory(cellData -> cellData.getValue().commentProperty());

        // Assuming we have a way to get the currently logged-in user's username
        String username = "logged_in_user";  // Replace with actual logged-in user identifier
        updateReviewsTable(reviewService.getReviewsByUser(username));
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            Parent courseSearchRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/virginia/sde/resources/CourseSearchView.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(courseSearchRoot, 600, 400));
            stage.setTitle("Course Search");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateReviewsTable(List<Review> reviews) {
        ObservableList<Review> reviewList = FXCollections.observableArrayList(reviews);
        reviewsTable.setItems(reviewList);
    }
}
