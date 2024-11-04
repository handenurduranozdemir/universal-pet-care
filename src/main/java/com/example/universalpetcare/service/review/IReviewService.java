package com.example.universalpetcare.service.review;

import com.example.universalpetcare.model.Review;
import com.example.universalpetcare.request.ReviewUpdateRequest;
import org.springframework.data.domain.Page;

public interface IReviewService {
    Review saveReview(Review review, Long userId, Long veterinarianId);
    double getAverageRatingForVet(Long veterinarianId);
    Page<Review> findAllReviewsByUserId(Long reviewerId, int page, int size);
    Review updateReview(Long reviewerId, ReviewUpdateRequest updateRequest);
    void deleteReview(Long reviewerId);
}
