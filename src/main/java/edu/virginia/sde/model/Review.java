package edu.virginia.sde.model;

import edu.virginia.sde.service.CourseService;
import edu.virginia.sde.service.CourseServiceImpl;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

public class Review {
    private final IntegerProperty reviewId;
    private final StringProperty username;
    private final IntegerProperty courseId;
    private final IntegerProperty rating;
    private final StringProperty comment;

    // Reference to CourseService to fetch course details
    private final CourseService courseService = new CourseServiceImpl();

    public Review(int reviewId, String username, int courseId, int rating, String comment) {
        this.reviewId = new SimpleIntegerProperty(reviewId);
        this.username = new SimpleStringProperty(username);
        this.courseId = new SimpleIntegerProperty(courseId);
        this.rating = new SimpleIntegerProperty(rating);
        this.comment = new SimpleStringProperty(comment);
    }

    public IntegerProperty reviewIdProperty() {
        return reviewId;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public IntegerProperty courseIdProperty() {
        return courseId;
    }

    public IntegerProperty ratingProperty() {
        return rating;
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public int getReviewId() {
        return reviewId.get();
    }

    public String getUsername() {
        return username.get();
    }

    public int getCourseId() {
        return courseId.get();
    }

    public int getRating() {
        return rating.get();
    }

    public String getComment() {
        return comment.get();
    }

    public void setReviewId(int reviewId) {
        this.reviewId.set(reviewId);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setCourseId(int courseId) {
        this.courseId.set(courseId);
    }

    public void setRating(int rating) {
        this.rating.set(rating);
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    // Implementing courseProperty to return the course title
    public ObservableValue<String> courseProperty() {
        Course course = courseService.getCourseById(courseId.get());
        String courseTitle = (course != null) ? course.getTitle() : "Unknown Course";
        return new SimpleStringProperty(courseTitle);
    }
}
