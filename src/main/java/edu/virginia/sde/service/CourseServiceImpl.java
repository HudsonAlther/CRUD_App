package edu.virginia.sde.service;

import edu.virginia.sde.database.DatabaseInitializer;
import edu.virginia.sde.model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseServiceImpl implements CourseService {

    private static final String DB_URL = "jdbc:sqlite:course_reviews.db";

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT subject, number, title, (SELECT AVG(rating) FROM Reviews WHERE course_id = Courses.id) AS averageRating FROM Courses";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String subject = rs.getString("subject");
                int number = rs.getInt("number");
                String title = rs.getString("title");
                double averageRating = rs.getDouble("averageRating");

                courses.add(new Course(subject, number, title, averageRating));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }

    @Override
    public List<Course> searchCourses(String subject, String number, String title) {
        List<Course> results = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT subject, number, title, (SELECT AVG(rating) FROM Reviews WHERE course_id = Courses.id) AS averageRating FROM Courses WHERE 1=1");

        if (subject != null && !subject.isEmpty()) query.append(" AND subject LIKE ?");
        if (number != null && !number.isEmpty()) query.append(" AND number = ?");
        if (title != null && !title.isEmpty()) query.append(" AND title LIKE ?");

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {

            int index = 1;
            if (subject != null && !subject.isEmpty()) pstmt.setString(index++, "%" + subject + "%");
            if (number != null && !number.isEmpty()) pstmt.setInt(index++, Integer.parseInt(number));
            if (title != null && !title.isEmpty()) pstmt.setString(index, "%" + title + "%");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String courseSubject = rs.getString("subject");
                int courseNumber = rs.getInt("number");
                String courseTitle = rs.getString("title");
                double averageRating = rs.getDouble("averageRating");

                results.add(new Course(courseSubject, courseNumber, courseTitle, averageRating));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    @Override
    public boolean addCourse(Course course) {
        String insertQuery = "INSERT INTO Courses (subject, number, title) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setString(1, course.getSubject().trim());
            pstmt.setInt(2, course.getNumber());
            pstmt.setString(3, course.getTitle().trim());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.err.println("[ERROR] Course already exists in the database.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public Course getCourseById(int courseId) {
        String query = "SELECT subject, number, title, (SELECT AVG(rating) FROM Reviews WHERE course_id = Courses.id) AS averageRating FROM Courses WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String subject = rs.getString("subject");
                int number = rs.getInt("number");
                String title = rs.getString("title");
                double averageRating = rs.getDouble("averageRating");

                return new Course(subject, number, title, averageRating);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
