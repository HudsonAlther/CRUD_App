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
        List<Review> userReviews = new ArrayList<>();
        String query = "SELECT r.id, r.rating, r.comment, r.timestamp, r.course_id, c.subject, c.number, c.title " +
                "FROM Reviews r " +
                "JOIN Users u ON r.user_id = u.id " +
                "JOIN Courses c ON r.course_id = c.id " +
                "WHERE u.username = ?";
        try (Connection conn = DatabaseInitializer.getConnection()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, username);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int reviewId = rs.getInt("id");
                        int courseId = rs.getInt("course_id");
                        int rating = rs.getInt("rating");
                        String comment = rs.getString("comment");
                        String subject = rs.getString("subject");
                        int number = rs.getInt("number");
                        String title = rs.getString("title");
                        Review review = new Review(reviewId, username, courseId, title, rating, comment);
                        userReviews.add(review);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch reviews: " + e.getMessage());
            e.printStackTrace();
        }
        return userReviews;
    }


    @Override
    public boolean addReview(Review review) {
        String query = "INSERT INTO Reviews (user_id, course_id, rating, comment, timestamp) " +
                "VALUES ((SELECT id FROM Users WHERE username = ?), ?, ?, ?, DATETIME('now'))";

        try (Connection conn = DatabaseInitializer.getConnection()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, review.getUsername());
                stmt.setInt(2, review.getCourseId());
                stmt.setInt(3, review.getRating());
                stmt.setString(4, review.getComment());

                int affectedRows = stmt.executeUpdate();

                // Debugging statement
                if (affectedRows > 0) {
                    System.out.println("[DEBUG] Successfully added review for user: " + review.getUsername() + ", Course ID: " + review.getCourseId());
                } else {
                    System.out.println("[DEBUG] Failed to add review for user: " + review.getUsername());
                }

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

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

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

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to delete review: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Review> getReviewById(int reviewId) {
        String query = "SELECT r.id, r.rating, r.comment, r.timestamp, u.username, c.id AS course_id, c.subject, c.number, c.title " +
                "FROM Reviews r " +
                "JOIN Users u ON r.user_id = u.id " +
                "JOIN Courses c ON r.course_id = c.id " +
                "WHERE r.id = ?";

        try (Connection conn = DatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, reviewId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Extract review details
                    int courseId = rs.getInt("course_id");
                    int rating = rs.getInt("rating");
                    String comment = rs.getString("comment");
                    String subject = rs.getString("subject");
                    int number = rs.getInt("number");
                    String title = rs.getString("title");
                    String username = rs.getString("username");

                    // Create course title
                    String courseTitle = subject + " " + number + ": " + title;

                    // Create a Review object and return it
                    Review review = new Review(reviewId, username, courseId, courseTitle, rating, comment);
                    return Optional.of(review);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch review by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT r.id, r.rating, r.comment, r.timestamp, u.username, c.id AS course_id, c.subject, c.number, c.title " +
                "FROM Reviews r " +
                "JOIN Users u ON r.user_id = u.id " +
                "JOIN Courses c ON r.course_id = c.id";
        try (Connection conn = DatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int reviewId = rs.getInt("id");
                int courseId = rs.getInt("course_id");
                int rating = rs.getInt("rating");
                String comment = rs.getString("comment");
                String subject = rs.getString("subject");
                int number = rs.getInt("number");
                String title = rs.getString("title");
                String username = rs.getString("username");

                // Create course title
                String courseTitle = subject + " " + number + ": " + title;

                // Create a Review object and add it to the list
                Review review = new Review(reviewId, username, courseId, courseTitle, rating, comment);
                reviews.add(review);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch all reviews: " + e.getMessage());
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public List<Review> searchReviews(String keyword) {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT r.id, r.rating, r.comment, r.timestamp, u.username, c.id AS course_id, c.subject, c.number, c.title " +
                "FROM Reviews r " +
                "JOIN Users u ON r.user_id = u.id " +
                "JOIN Courses c ON r.course_id = c.id " +
                "WHERE r.comment LIKE ? OR c.title LIKE ?";

        try (Connection conn = DatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int reviewId = rs.getInt("id");
                    int courseId = rs.getInt("course_id");
                    int rating = rs.getInt("rating");
                    String comment = rs.getString("comment");
                    String subject = rs.getString("subject");
                    int number = rs.getInt("number");
                    String title = rs.getString("title");
                    String username = rs.getString("username");

                    // Create course title
                    String courseTitle = subject + " " + number + ": " + title;

                    // Create a Review object
                    Review review = new Review(reviewId, username, courseId, courseTitle, rating, comment);
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to search reviews: " + e.getMessage());
            e.printStackTrace();
        }
        return reviews;
    }
}
