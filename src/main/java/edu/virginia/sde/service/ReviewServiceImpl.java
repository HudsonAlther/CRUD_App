package edu.virginia.sde.service;

import edu.virginia.sde.model.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReviewServiceImpl implements ReviewService {

    // Internal storage for reviews (can be replaced with a database in the future)
    private final List<Review> reviews = new ArrayList<>();

    @Override
    public List<Review> getReviewsByUser(String username) {
        return reviews.stream()
                .filter(review -> review.getUsername().equals(username))
                .collect(Collectors.toList());
    }


    @Override
    public List<Review> getAllReviews() {
        // Return all reviews
        return new ArrayList<>(reviews);
    }

    @Override
    public List<Review> searchReviews(String keyword) {
        // Search by keyword in comments (case-insensitive)
        return reviews.stream()
                .filter(review -> review.getComment().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean addReview(Review review) {
        // Prevent duplicate reviews by ID
        if (reviews.stream().anyMatch(r -> r.getReviewId() == review.getReviewId())) {
            return false;
        }
        return reviews.add(review);
    }

    @Override
    public boolean updateReview(Review review) {
        // Find the review by ID and update it
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
        // Remove the review if it exists
        return reviews.removeIf(r -> r.getReviewId() == review.getReviewId());
    }

    @Override
    public Optional<Review> getReviewById(int reviewId) {
        // Use streams to find a review by ID
        return reviews.stream()
                .filter(review -> review.getReviewId() == reviewId)
                .findFirst();
    }
}
