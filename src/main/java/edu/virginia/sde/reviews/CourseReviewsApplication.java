package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class CourseReviewsApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Course Reviews Application - Login");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/virginia/sde/hw6/gui/LoginView.fxml")));
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }
}