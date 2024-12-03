package edu.virginia.sde.model;

import javafx.beans.property.*;

public class Course {
    private final StringProperty subject;
    private final IntegerProperty number;
    private final StringProperty title;
    private final DoubleProperty averageRating;

    public Course(String subject, int number, String title, double averageRating) {
        this.subject = new SimpleStringProperty(subject);
        this.number = new SimpleIntegerProperty(number);
        this.title = new SimpleStringProperty(title);
        this.averageRating = new SimpleDoubleProperty(averageRating);
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public IntegerProperty numberProperty() {
        return number;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public DoubleProperty averageRatingProperty() {
        return averageRating;
    }
}
