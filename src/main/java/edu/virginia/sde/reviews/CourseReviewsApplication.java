package edu.virginia.sde.reviews;

import edu.virginia.sde.database.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.Objects;

public class CourseReviewsApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Connection connection = DatabaseInitializer.initializeDatabase();
        if (connection != null) {
            DatabaseInitializer.populateSampleData(connection); // Pass connection for sample data
        } else {
            System.err.println("[ERROR] Database initialization failed. Exiting...");
            return;
        }

        try {
            System.out.println("[INFO] Loading FXML file...");
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/virginia/sde/reviews/LoginView.fxml")));
            primaryStage.setTitle("Course Reviews Application - Login");
            primaryStage.setScene(new Scene(root, 400, 300));
            primaryStage.show();
            System.out.println("[INFO] Application started successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("[ERROR] Failed to load FXML file.");
        }
    }
}
