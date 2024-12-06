package edu.virginia.sde.controller;

import edu.virginia.sde.model.Course;
import edu.virginia.sde.model.Review;
import edu.virginia.sde.service.ReviewService;
import edu.virginia.sde.service.ReviewServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;

public class CourseReviewsController {

    @FXML
    private TableView<Review> reviewsTable;
    @FXML
    private TableColumn<Review, String> usernameColumn;
    @FXML
    private TableColumn<Review, Integer> ratingColumn;
    @FXML
    private TableColumn<Review, String> commentColumn;

    private final ReviewService reviewService = new ReviewServiceImpl();

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().ratingProperty().asObject());
        commentColumn.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
        reviewsTable.setPlaceholder(new Label("No reviews available."));
    }

    public void setCourse(Course course) {
        List<Review> reviews = reviewService.getReviewsByCourseId(course.getId());
        ObservableList<Review> reviewList = FXCollections.observableArrayList(reviews);
        reviewsTable.setItems(reviewList);
    }

    @FXML
    public void handleBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
