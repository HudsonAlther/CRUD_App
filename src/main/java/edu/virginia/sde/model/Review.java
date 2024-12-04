package edu.virginia.sde.model;

import javafx.beans.property.*;

public class Review {
    private final IntegerProperty reviewId;
    private final StringProperty username;
    private final StringProperty courseTitle; // Preloaded course title
    private final IntegerProperty rating;
    private final StringProperty comment;

    public Review(int reviewId, String username, String courseTitle, int rating, String comment) {
        this.reviewId = new SimpleIntegerProperty(reviewId);
        this.username = new SimpleStringProperty(username);
        this.courseTitle = new SimpleStringProperty(courseTitle); // Preload course title
        this.rating = new SimpleIntegerProperty(rating);
        this.comment = new SimpleStringProperty(comment);
    }

    // Properties for JavaFX binding
    public IntegerProperty reviewIdProperty() {
        return reviewId;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty courseProperty() {
        return courseTitle;
    }

    public IntegerProperty ratingProperty() {
        return rating;
    }

    public StringProperty commentProperty() {
        return comment;
    }

    // Getters
    public int getReviewId() {
        return reviewId.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getCourseTitle() {
        return courseTitle.get();
    }

    public int getRating() {
        return rating.get();
    }

    public String getComment() {
        return comment.get();
    }

    // Setters
    public void setReviewId(int reviewId) {
        this.reviewId.set(reviewId);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle.set(courseTitle);
    }

    public void setRating(int rating) {
        this.rating.set(rating);
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }
}
