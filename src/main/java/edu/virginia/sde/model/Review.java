package edu.virginia.sde.model;

import javafx.beans.property.*;

import java.sql.Timestamp;

public class Review {
    private final IntegerProperty reviewId;
    private final StringProperty username;
    private final IntegerProperty courseId;
    private final IntegerProperty rating;
    private final StringProperty comment;
    private final StringProperty courseTitle;
    private StringProperty timestamp = null;

    public Review(int reviewId, String username, int courseId, int rating, String comment, String courseTitle) {
        this.reviewId = new SimpleIntegerProperty(reviewId);
        this.username = new SimpleStringProperty(username);
        this.courseId = new SimpleIntegerProperty(courseId);
        this.rating = new SimpleIntegerProperty(rating);
        this.comment = new SimpleStringProperty(comment);
        this.courseTitle = new SimpleStringProperty(courseTitle);
        this.timestamp = new SimpleStringProperty(new Timestamp(System.currentTimeMillis()).toString());

    }

    // Property getter for JavaFX bindings
    public StringProperty courseTitleProperty() {
        return courseTitle;
    }

    public StringProperty timestampProperty() {
        return timestamp;
    }

    public String getTimestamp() {
        return timestamp.get();
    }

    public String getCourseTitle() {
        return courseTitle.get();
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle.set(courseTitle);
    }

    // Other properties and methods
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

    public void setReviewId(int reviewId) {
        this.reviewId.set(reviewId);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public int getCourseId() {
        return courseId.get();
    }

    public void setCourseId(int courseId) {
        this.courseId.set(courseId);
    }

    public int getRating() {
        return rating.get();
    }

    public void setRating(int rating) {
        this.rating.set(rating);
    }

    public String getComment() {
        return comment.get();
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    // Updated getUserId method to return username as user ID
    public String getUserId() {
        return getUsername();
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + getReviewId() +
                ", username='" + getUsername() + '\'' +
                ", courseId=" + getCourseId() +
                ", rating=" + getRating() +
                ", comment='" + getComment() + '\'' +
                ", courseTitle='" + getCourseTitle() + '\'' +
                '}';
    }
}
