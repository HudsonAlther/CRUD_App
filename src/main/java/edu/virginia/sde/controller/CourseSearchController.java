package edu.virginia.sde.controller;

import edu.virginia.sde.model.Course;
import edu.virginia.sde.service.CourseService;
import edu.virginia.sde.service.CourseServiceImpl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
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
    private TableColumn<Course, Void> viewReviewsColumn = new TableColumn<>("Reviews");

    private String username;

    @FXML
    public void initialize() {
        // Bind table columns to Course properties
        subjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        numberColumn.setCellValueFactory(cellData -> cellData.getValue().numberProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().averageRatingProperty().asObject());

        addViewReviewsButtonToTable();

        refreshCourseTable();
    }

    private void addViewReviewsButtonToTable() {
        Callback<TableColumn<Course, Void>, TableCell<Course, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Course, Void> call(final TableColumn<Course, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("View Reviews");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Course course = getTableView().getItems().get(getIndex());
                            handleViewReviews(course);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        viewReviewsColumn.setCellFactory(cellFactory);
    }




    private void handleViewReviews(Course course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/virginia/sde/reviews/CourseReviewsView.fxml"));

            // Debugging output for FXML resource loading
            URL resource = getClass().getResource("/edu/virginia/sde/reviews/CourseReviewsView.fxml");
            if (resource == null) {
                System.err.println("[ERROR] Could not locate CourseReviewsView.fxml!");
                return;
            } else {
                System.out.println("[INFO] Loading FXML file from: " + resource);
            }
            Parent root = loader.load();
            edu.virginia.sde.reviews.CourseReviewsController controller = loader.getController();
            if (controller != null) {
                controller.setCourse(course);
            } else {
                throw new IllegalStateException("CourseReviewsController could not be initialized properly.");
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Reviews for " + course.getTitle());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Course Reviews screen. Please check the FXML path and content.");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to initialize the Course Reviews screen properly.");
        }
    }


    public void handleViewReviews(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/virginia/sde/reviews/CourseReviewsView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Course Reviews");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Course Reviews screen.");
        }
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

    @FXML
    public void handleMyReviews(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/virginia/sde/reviews/MyReviewsView.fxml"));
            Parent root = loader.load();

            // Assuming MyReviewsController has a setUsername() method
            MyReviewsController controller = loader.getController();
            controller.setUsername(username);

            Stage stage = new Stage();
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
