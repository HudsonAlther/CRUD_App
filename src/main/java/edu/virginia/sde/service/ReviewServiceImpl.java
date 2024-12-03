package edu.virginia.sde.service.impl;

import edu.virginia.sde.model.Review;
import edu.virginia.sde.service.ReviewService;

import java.util.ArrayList;
import java.util.List;

public class ReviewServiceImpl implements ReviewService {

    // Placeholder for actual database integration
    private final List<Review> reviews = new ArrayList<>();

    @Override
    public List<Review> getReviewsByUser(String username) {
        // Simulated logic to retrieve reviews by username
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
        return reviews.add(review);  // Actual implementation will interact with the database
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
