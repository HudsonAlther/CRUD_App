package edu.virginia.sde.service;

import edu.virginia.sde.model.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getReviewsByUser(String username);
    boolean addReview(Review review);
    boolean updateReview(Review review);
    boolean deleteReview(Review review);
}
