package edu.virginia.sde.controller;

import edu.virginia.sde.model.Course;
import edu.virginia.sde.service.CourseService;
import edu.virginia.sde.service.CourseServiceImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class CourseSearchController {

    @FXML
    private TableView<Course> courseTable;

    private String currentUser;

    @FXML
    private TableColumn<Course, String> subjectColumn;

    @FXML
    private TableColumn<Course, Integer> numberColumn;

    @FXML
    private TableColumn<Course, String> titleColumn;

    @FXML
    private TableColumn<Course, Double> ratingColumn;

    @FXML
    private TextField subjectField;

    @FXML
    private TextField numberField;

    @FXML
    private TextField titleField;

    @FXML
    private TextField searchField;

    @FXML
    private VBox addCourseForm;

    private final CourseService courseService = new CourseServiceImpl();

    @FXML
    public void initialize() {
        subjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        numberColumn.setCellValueFactory(cellData -> cellData.getValue().numberProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().averageRatingProperty().asObject());

        refreshCourseTable();
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText().trim();
        List<Course> results = courseService.searchCourses(query, query, query);
        courseTable.setItems(FXCollections.observableArrayList(results));
    }

    @FXML
    public void handleAddCourse() {
        String subject = subjectField.getText().trim().toUpperCase();
        String number = numberField.getText().trim();
        String title = titleField.getText().trim();

        if (!subject.matches("[A-Z]{2,4}") || !number.matches("\\d{4}") || title.length() < 1 || title.length() > 50) {
            showAlert("Error", "Invalid input. Please check your data and try again.");
            return;
        }

        Course course = new Course(subject, Integer.parseInt(number), title, 0.0);

        if (courseService.addCourse(course)) {
            showAlert("Success", "Course added successfully!");
            refreshCourseTable();
        } else {
            showAlert("Error", "Failed to add course. It may already exist.");
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

    public void setCurrentUser(String username) {
        this.currentUser = username;
    }
}
