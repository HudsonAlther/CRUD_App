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

    public String getSubject() {
        return subject.get();
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public int getNumber() {
        return number.get();
    }

    public void setNumber(int number) {
        this.number.set(number);
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public double getAverageRating() {
        return averageRating.get();
    }

    public void setAverageRating(double averageRating) {
        this.averageRating.set(averageRating);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        return number.get() == course.number.get() &&
                subject.get().equalsIgnoreCase(course.subject.get()) &&
                title.get().equalsIgnoreCase(course.title.get());
    }

    @Override
    public int hashCode() {
        int result = subject.get().toLowerCase().hashCode();
        result = 31 * result + number.get();
        result = 31 * result + title.get().toLowerCase().hashCode();
        return result;
    }
}
