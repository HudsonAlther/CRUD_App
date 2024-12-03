package edu.virginia.sde.service;
import edu.virginia.sde.model.Course;
import edu.virginia.sde.model.Review;

import java.util.ArrayList;
import java.util.List;

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
            // Placeholder implementation
            return true;
        }

        @Override
        public boolean deleteReview(Review review) {
            return reviews.remove(review);
        }
    }
}
