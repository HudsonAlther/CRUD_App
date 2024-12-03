package edu.virginia.sde.service;
import edu.virginia.sde.model.Course;
import edu.virginia.sde.model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static edu.virginia.sde.service.UserServiceImpl.DB_URL;

public interface CourseService {
    List<Course> getAllCourses();
    List<Course> searchCourses(String subject, String number, String title);
    boolean addCourse(Course course);

    class ReviewServiceImpl implements ReviewService {

        private final List<Review> reviews = new ArrayList<>();

        @Override
        public List<Review> getReviewsByUser(String username) {
            // Simulated logic
            List<Review> userReviews = new ArrayList<>();
            for (Review review : reviews) {
                if (review.courseProperty().get().equals(username)) {
                    userReviews.add(review);
                }
            }
            return userReviews;
        }

        @Override
        public boolean addReview(Review review) {
            return reviews.add(review);
        }

        @Override
        public boolean updateReview(Review review) {
            String query = "UPDATE reviews SET rating = ?, comment = ?, timestamp = ? WHERE review_id = ?";
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, review.getRating());
                preparedStatement.setString(2, review.getComment());
                preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                preparedStatement.setInt(4, review.getReviewId());

                return preparedStatement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }


        @Override
        public boolean deleteReview(Review review) {
            return reviews.remove(review);
        }
    }
}
