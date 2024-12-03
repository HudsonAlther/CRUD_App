package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CourseReviewsApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("Loading FXML file...");
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/virginia/sde/reviews/LoginView.fxml")));
            if (root == null) {
                throw new NullPointerException("FXML file not found: LoginView.fxml");
            }

            primaryStage.setTitle("Course Reviews Application - Login");
            primaryStage.setScene(new Scene(root, 400, 300));
            primaryStage.show();
            System.out.println("Application started successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load FXML file.");
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("NullPointerException: Could not load FXML resource.");
        }
    }

}