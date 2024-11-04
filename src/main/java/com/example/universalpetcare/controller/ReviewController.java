package com.example.universalpetcare.controller;

import com.example.universalpetcare.dto.ReviewDto;
import com.example.universalpetcare.exceptions.AlreadyExistsException;
import com.example.universalpetcare.exceptions.ResourceNotFoundException;
import com.example.universalpetcare.model.Review;
import com.example.universalpetcare.request.ReviewUpdateRequest;
import com.example.universalpetcare.response.ApiResponse;
import com.example.universalpetcare.service.review.IReviewService;
import com.example.universalpetcare.utils.FeedBackMessages;
import com.example.universalpetcare.utils.UrlMapping;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(UrlMapping.REVIEW)
public class ReviewController {
    private final IReviewService reviewService;
    private final ModelMapper modelMapper;

    @PostMapping(UrlMapping.SUBMIT_REVIEW)
    public ResponseEntity<ApiResponse> saveReview(@RequestParam Long veterinarianId,
                                                  @RequestParam Long reviewerId,
                                                  @RequestBody Review review)
    {
        try {
            Review savedReview = reviewService.saveReview(review, reviewerId, veterinarianId);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessages.CREATE_SUCCESS, savedReview.getFeedback() + savedReview.getStars()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(NOT_ACCEPTABLE).body(new ApiResponse(e.getMessage(),null));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMapping.GET_USER_REVIEWS)
    public ResponseEntity<ApiResponse> getReviewByUserId(@PathVariable Long userId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int size)
    {
        Page<Review> reviewPage = reviewService.findAllReviewsByUserId(userId, page, size);
        Page<ReviewDto> reviewDtos = reviewPage.map((element) -> modelMapper.map(element, ReviewDto.class));
        return ResponseEntity.status(FOUND).body(new ApiResponse(FeedBackMessages.RESOURCE_FOUND, reviewDtos));
    }

    @PutMapping(UrlMapping.UPDATE_REVIEW)
    public ResponseEntity<ApiResponse> updateReview(@PathVariable Long reviewId,
                                                    @RequestBody ReviewUpdateRequest updateRequest)
    {
        try {
            Review updatedReview = reviewService.updateReview(reviewId, updateRequest);
            return ResponseEntity.ok(new ApiResponse(
                    FeedBackMessages.UPDATE_SUCCESS, updatedReview.getFeedback() + " " + updatedReview.getStars()
            ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping(UrlMapping.DELETE_REVIEW)
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewerId)
    {
        try {
            reviewService.deleteReview(reviewerId);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessages.DELETE_SUCCESS,null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping(UrlMapping.GET_AVERAGE_RATING)
    public ResponseEntity<ApiResponse> getAverageRatingForVet(@PathVariable Long vetId)
    {
        double averageRating = reviewService.getAverageRatingForVet(vetId);
        return  ResponseEntity.ok(new ApiResponse(FeedBackMessages.RESOURCE_FOUND,averageRating));
    }
}
