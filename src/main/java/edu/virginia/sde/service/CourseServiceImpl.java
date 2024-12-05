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
        String query = "SELECT id, subject, number, title, (SELECT AVG(rating) FROM Reviews WHERE course_id = Courses.id) AS averageRating FROM Courses";
        List<Course> courses = new ArrayList<>();

        try (Connection conn = DatabaseInitializer.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("subject"),
                        rs.getInt("number"),
                        rs.getString("title"),
                        rs.getDouble("averageRating")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch courses: " + e.getMessage());
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public List<Course> searchCourses(String subject, String number, String title) {
        List<Course> results = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT id, subject, number, title, (SELECT AVG(rating) FROM Reviews WHERE course_id = Courses.id) AS averageRating FROM Courses WHERE 1=1");

        // Dynamically append conditions
        if (subject != null && !subject.isBlank()) queryBuilder.append(" AND subject LIKE ?");
        if (number != null && !number.isBlank()) queryBuilder.append(" AND number = ?");
        if (title != null && !title.isBlank()) queryBuilder.append(" AND title LIKE ?");

        try (Connection conn = DatabaseInitializer.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {

            int paramIndex = 1;
            if (subject != null && !subject.isBlank()) pstmt.setString(paramIndex++, "%" + subject.trim() + "%");
            if (number != null && !number.isBlank()) pstmt.setInt(paramIndex++, Integer.parseInt(number.trim()));
            if (title != null && !title.isBlank()) pstmt.setString(paramIndex++, "%" + title.trim() + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new Course(
                            rs.getInt("id"),
                            rs.getString("subject"),
                            rs.getInt("number"),
                            rs.getString("title"),
                            rs.getDouble("averageRating")
                    ));
                }
            }
        } catch (SQLException | NumberFormatException e) {
            System.err.println("[ERROR] Failed to search courses: " + e.getMessage());
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
                System.err.println("[ERROR] Failed to add course: " + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public Course getCourseById(int courseId) {
        String query = "SELECT subject, number, title, " +
                "(SELECT ROUND(AVG(rating), 2) FROM Reviews WHERE course_id = Courses.id) AS averageRating " +
                "FROM Courses WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Course(
                            rs.getInt("id"),
                            rs.getString("subject"),
                            rs.getInt("number"),
                            rs.getString("title"),
                            rs.getDouble("averageRating")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch course by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
