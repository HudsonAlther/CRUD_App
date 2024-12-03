package edu.virginia.sde.controller;
import edu.virginia.sde.model.*;
import edu.virginia.sde.service.*;
import edu.virginia.sde.service.CourseServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class CourseSearchController {

    @FXML
    private TextField subjectField;
    @FXML
    private TextField numberField;
    @FXML
    private TextField titleField;
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

    private final CourseService courseService = new CourseServiceImpl();

    @FXML
    public void initialize() {
        subjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        numberColumn.setCellValueFactory(cellData -> cellData.getValue().numberProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().averageRatingProperty().asObject());

        updateCourseTable(courseService.getAllCourses());
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String subject = subjectField.getText().trim();
        String number = numberField.getText().trim();
        String title = titleField.getText().trim();
        List<Course> filteredCourses = courseService.searchCourses(subject, number, title);
        updateCourseTable(filteredCourses);
    }


    @FXML
    public void handleAddCourse(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Course");
        dialog.setHeaderText("Enter Course Details");

        // Create input fields
        TextField subjectField = new TextField();
        subjectField.setPromptText("Subject (e.g., CS)");
        TextField numberField = new TextField();
        numberField.setPromptText("Course Number (e.g., 3140)");
        TextField titleField = new TextField();
        titleField.setPromptText("Course Title");

        // Layout for input fields
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                new Label("Subject:"), subjectField,
                new Label("Number:"), numberField,
                new Label("Title:"), titleField
        );
        dialog.getDialogPane().setContent(vbox);

        // Add buttons
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String subject = subjectField.getText().trim();
                String number = numberField.getText().trim();
                String title = titleField.getText().trim();

                if (!subject.isEmpty() && !number.isEmpty() && !title.isEmpty()) {
                    try {
                        int courseNumber = Integer.parseInt(number);
                        Course newCourse = new Course(subject, courseNumber, title, 0.0);
                        boolean added = courseService.addCourse(newCourse);
                        if (added) {
                            updateCourseTable(courseService.getAllCourses());
                        } else {
                            System.out.println("Failed to add course. Course might already exist.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid course number. Must be an integer.");
                    }
                } else {
                    System.out.println("All fields must be filled.");
                }
            }
        });
    }


    @FXML
    public void handleMyReviews(ActionEvent event) {
        try {
            Parent myReviewsRoot = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/hw6/gui/MyReviewsView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(myReviewsRoot, 600, 400));
            stage.setTitle("My Reviews");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/virginia/sde/hw6/gui/LoginView.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginRoot, 400, 300));
            stage.setTitle("Course Reviews Application - Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCourseTable(List<Course> courses) {
        ObservableList<Course> courseList = FXCollections.observableArrayList(courses);
        courseTable.setItems(courseList);
    }

}
