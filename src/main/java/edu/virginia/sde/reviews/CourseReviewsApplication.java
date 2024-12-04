package edu.virginia.sde.reviews;

import edu.virginia.sde.database.DatabaseInitializer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;

public class CourseReviewsApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize database and populate default courses
        try (Connection connection = DatabaseInitializer.initializeDatabase()) {
            if (connection != null) {
                DatabaseInitializer.populateDefaultCourses(connection);
            } else {
                System.err.println("[ERROR] Database initialization failed. Exiting...");
                Platform.exit();
                return;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Unexpected error during database setup: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
            return;
        }

        // Load the LoginView FXML
        try {
            System.out.println("[INFO] Loading FXML file...");
            URL fxmlResource = getClass().getResource("/edu/virginia/sde/reviews/LoginView.fxml");
            if (fxmlResource == null) {
                System.err.println("[ERROR] LoginView.fxml not found. Exiting application.");
                Platform.exit();
                return;
            }
            Parent root = FXMLLoader.load(fxmlResource);
            primaryStage.setTitle("Course Reviews Application - Login");
            primaryStage.setScene(new Scene(root));
            primaryStage.sizeToScene();
            primaryStage.show();
            System.out.println("[INFO] Application started successfully.");
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to load FXML file: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }
    }
}
