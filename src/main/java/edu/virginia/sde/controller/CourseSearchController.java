package edu.virginia.sde.controller;

import edu.virginia.sde.model.Course;
import edu.virginia.sde.managers.SessionManager;
import edu.virginia.sde.service.CourseService;
import edu.virginia.sde.service.CourseServiceImpl;
import edu.virginia.sde.service.ReviewServiceImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;

public class CourseSearchController {

    @FXML
    private TextField subjectField;

    @FXML
    private TextField numberField;

    @FXML
    private TextField titleField;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private TableView<Course> courseTable;

    @FXML
    private TableColumn<Course, String> subjectColumn;

    @FXML
    private TableColumn<Course, Integer> numberColumn;

    @FXML
    private TableColumn<Course, String> titleColumn;

    @FXML
    private TableColumn<Course, Double> ratingColumn;

    private String username;

    private final CourseService courseService = new CourseServiceImpl();

    @FXML
    public void initialize() {
        // Bind table columns to Course properties
        subjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        numberColumn.setCellValueFactory(cellData -> cellData.getValue().numberProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().averageRatingProperty().asObject());

        refreshCourseTable();
    }

    @FXML
    public void handleSearch() {
        String subject = subjectField.getText().trim();
        String number = numberField.getText().trim();
        String title = titleField.getText().trim();

        List<Course> results;
        if (!subject.isEmpty() || !number.isEmpty() || !title.isEmpty()) {
            results = courseService.searchCourses(subject, number, title);
        } else {
            String query = searchField.getText().trim();
            results = courseService.searchCourses(query, query, query);
        }

        courseTable.setItems(FXCollections.observableArrayList(results));
    }

    @FXML
    public void handleAddCourse() {
        String subject = subjectField.getText().trim().toUpperCase();
        String number = numberField.getText().trim();
        String title = titleField.getText().trim();

        if (!subject.matches("[A-Z]{2,4}") || !number.matches("\\d{4}") || title.isEmpty() || title.length() > 50) {
            showAlert("Error", "Invalid input. Please ensure:\n- Subject is 2-4 letters\n- Number is 4 digits\n- Title is 1-50 characters");
            return;
        }

        Course course = new Course(0, subject, Integer.parseInt(number), title, 0.0);

        if (courseService.addCourse(course)) {
            showAlert("Success", "Course added successfully!");
            refreshCourseTable();
        } else {
            showAlert("Error", "Failed to add course. It may already exist.");
        }
    }

    public void handleMyReviews(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/virginia/sde/reviews/MyReviewsView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("My Reviews");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the My Reviews screen.");
        }
    }





    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/virginia/sde/reviews/LoginView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Login screen.");
        }
    }

    private void refreshCourseTable() {
        List<Course> courses = courseService.getAllCourses();
        courseTable.setItems(FXCollections.observableArrayList(courses));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setUsername(String username) {
        this.username = username;
        System.out.println("Logged-in user: " + username);
    }

    @FXML
    public void handleWriteReview(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/virginia/sde/reviews/WriteReviewView.fxml"));
            Parent root = loader.load();

            WriteReviewController writeReviewController = loader.getController();
            writeReviewController.setUsername(username);

            Stage stage = new Stage();
            stage.setTitle("Write Review");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Write Review screen.");
        }
    }

}
