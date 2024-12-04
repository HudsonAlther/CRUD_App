package edu.virginia.sde.service;

import edu.virginia.sde.database.DatabaseInitializer;
import edu.virginia.sde.model.Course;
import edu.virginia.sde.model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewServiceImpl implements ReviewService {

    @Override
    public List<Review> getReviewsByUser(String username) {
        String query = """
    SELECT r.id, r.rating, r.comment, r.timestamp, r.course_id, c.subject, c.number, c.title, u.username
    FROM Reviews r
    JOIN Users u ON r.user_id = u.id
    JOIN Courses c ON r.course_id = c.id
    WHERE u.username = ?
""";


        List<Review> userReviews = new ArrayList<>();
        try (Connection conn = DatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    userReviews.add(mapResultSetToReview(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch reviews for user: " + username);
            e.printStackTrace();
        }
        return userReviews;
    }

    @Override
    public List<Review> getAllReviews() {
        String query = """
            SELECT r.id, r.rating, r.comment, r.timestamp, r.course_id, u.username, c.subject, c.number, c.title
            FROM Reviews r
            JOIN Users u ON r.user_id = u.id
            JOIN Courses c ON r.course_id = c.id
        """;

        List<Review> reviews = new ArrayList<>();
        try (Connection conn = DatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                reviews.add(mapResultSetToReview(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch all reviews.");
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public List<Review> searchReviews(String keyword) {
        return null;
    }

    @Override
    public boolean addReview(Review review) {
        String getUserIdQuery = "SELECT id FROM Users WHERE username = ?";
        String insertReviewQuery = "INSERT INTO Reviews (user_id, course_id, rating, comment, timestamp) " +
                "VALUES (?, ?, ?, ?, DATETIME('now'))";

        try (Connection conn = DatabaseInitializer.getConnection()) {
            int userId;

            // Step 1: Resolve user_id
            try (PreparedStatement userStmt = conn.prepareStatement(getUserIdQuery)) {
                userStmt.setString(1, review.getUsername());
                try (ResultSet rs = userStmt.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getInt("id");
                    } else {
                        System.err.println("[ERROR] Username not found: " + review.getUsername());
                        return false; // Fail if user doesn't exist
                    }
                }
            }

            // Step 2: Insert the review
            try (PreparedStatement insertStmt = conn.prepareStatement(insertReviewQuery)) {
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, review.getCourseId());
                insertStmt.setInt(3, review.getRating());
                insertStmt.setString(4, review.getComment());

                int affectedRows = insertStmt.executeUpdate();
                System.out.println("[DEBUG] Inserted review. Affected rows: " + affectedRows);
                return affectedRows > 0;
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to add review: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public boolean updateReview(Review review) {
        String query = "UPDATE Reviews SET rating = ?, comment = ? WHERE id = ?";

        try (Connection conn = DatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, review.getRating());
            stmt.setString(2, review.getComment());
            stmt.setInt(3, review.getReviewId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to update review: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteReview(Review review) {
        String query = "DELETE FROM Reviews WHERE id = ?";

        try (Connection conn = DatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, review.getReviewId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to delete review: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Review> getReviewById(int reviewId) {
        String query = """
            SELECT r.id, r.rating, r.comment, r.timestamp, r.course_id, u.username, c.subject, c.number, c.title
            FROM Reviews r
            JOIN Users u ON r.user_id = u.id
            JOIN Courses c ON r.course_id = c.id
            WHERE r.id = ?
        """;

        try (Connection conn = DatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, reviewId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToReview(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch review by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Helper Methods
    private int validateUserId(Connection conn, String username, String query) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("User not found: " + username);
                }
            }
        }
    }

    private void validateCourseId(Connection conn, int courseId, String query) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Course not found: " + courseId);
                }
            }
        }
    }

    private Review mapResultSetToReview(ResultSet rs) throws SQLException {
        int reviewId = rs.getInt("id");
        int courseId = rs.getInt("course_id");
        int rating = rs.getInt("rating");
        String comment = rs.getString("comment");
        String subject = rs.getString("subject");
        int number = rs.getInt("number");
        String title = rs.getString("title");
        String username = rs.getString("username");

        // Combine subject and number for a meaningful course title
        String courseTitle = subject + " " + number + ": " + title;

        // Create the Review object
        return new Review(reviewId, username, courseId, rating, comment, courseTitle);
    }

}
