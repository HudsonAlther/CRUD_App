package edu.virginia.sde.model;

import javafx.beans.property.*;

public class Review {
    private final StringProperty course;
    private final IntegerProperty rating;
    private final StringProperty comment;

    public Review(String course, int rating, String comment) {
        this.course = new SimpleStringProperty(course);
        this.rating = new SimpleIntegerProperty(rating);
        this.comment = new SimpleStringProperty(comment);
    }

    public StringProperty courseProperty() {
        return course;
    }

    public IntegerProperty ratingProperty() {
        return rating;
    }

    public StringProperty commentProperty() {
        return comment;
    }
}
