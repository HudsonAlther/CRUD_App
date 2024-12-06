package edu.virginia.sde.model;
import edu.virginia.sde.database.DatabaseInitializer;
import javafx.beans.property.*;
import javafx.scene.input.Mnemonic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Course {
    private final IntegerProperty id;
    private final StringProperty subject;
    private final IntegerProperty number;
    private final StringProperty title;
    private final DoubleProperty averageRating;

    public Course(int id, String subject, int number, String title, double averageRating) {
        this.id = new SimpleIntegerProperty(id);
        this.subject = new SimpleStringProperty(subject);
        this.number = new SimpleIntegerProperty(number);
        this.title = new SimpleStringProperty(title);
        this.averageRating = new SimpleDoubleProperty(averageRating);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
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

    @Override
    public String toString() {
        return subject.get() + " " + number.get() + ": " + title.get();
    }

    public String getFormattedAverageRating() {
        return String.format("%.2f", averageRating.get());
    }
    public void refreshAverageRating() {
        String query = "SELECT ROUND(AVG(rating), 2) AS averageRating " +
                "FROM Reviews WHERE course_id = ?";
        try (Connection conn = DatabaseInitializer.getConnection()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, this.getId()); // Assuming getId() returns the course's ID
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    double newAverage = rs.getDouble("averageRating");
                    this.setAverageRating(newAverage); // Assuming setAverageRating() updates the property
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to refresh average rating: " + e.getMessage());
            e.printStackTrace();
        }
    }



}
