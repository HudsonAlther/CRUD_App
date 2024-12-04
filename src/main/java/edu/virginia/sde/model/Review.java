package edu.virginia.sde.model;

import javafx.beans.property.*;
 public class Review {
        private final IntegerProperty reviewId;
        private final StringProperty username;
        private final IntegerProperty courseId; // This is for internal use
        private final StringProperty courseTitle; // This is for UI display
        private final IntegerProperty rating;
        private final StringProperty comment;

        public Review(int reviewId, String username, int courseId, String courseTitle, int rating, String comment) {
            this.reviewId = new SimpleIntegerProperty(reviewId);
            this.username = new SimpleStringProperty(username);
            this.courseId = new SimpleIntegerProperty(courseId);
            this.courseTitle = new SimpleStringProperty(courseTitle);
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

    public IntegerProperty courseIdProperty() {
        return courseId;
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

    public int getCourseId() {
        return courseId.get();
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

    public void setCourseId(int courseId) {
        this.courseId.set(courseId);
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
