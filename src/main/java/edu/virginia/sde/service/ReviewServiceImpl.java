package edu.virginia.sde.service;

import edu.virginia.sde.model.*;
import edu.virginia.sde.service.ReviewService;

import java.util.ArrayList;
import java.util.List;

public class ReviewServiceImpl implements ReviewService {
    private final List<Review> reviews = new ArrayList<>();

    @Override
    public List<Review> getReviewsByUser(String username) {
        List<Review> userReviews = new ArrayList<>();
        for (Review review : reviews) {
            if (review.getUsername().equals(username)) {
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
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getReviewId() == review.getReviewId()) {
                reviews.set(i, review);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteReview(Review review) {
        return reviews.remove(review);
    }
}
