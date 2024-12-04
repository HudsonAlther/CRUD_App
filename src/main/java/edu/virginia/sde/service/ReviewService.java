package edu.virginia.sde.service;

import edu.virginia.sde.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<Review> getReviewsByUser(String username);
    List<Review> getAllReviews();
    List<Review> searchReviews(String keyword);
    boolean addReview(Review review);
    boolean updateReview(Review review);

    boolean deleteReview(int reviewId, String username);

    Optional<Review> getReviewById(int reviewId);
}
