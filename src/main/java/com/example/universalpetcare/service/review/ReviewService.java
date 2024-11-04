package com.example.universalpetcare.service.review;

import com.example.universalpetcare.enums.AppointmentStatus;
import com.example.universalpetcare.exceptions.ResourceNotFoundException;
import com.example.universalpetcare.exceptions.AlreadyExistsException;
import com.example.universalpetcare.model.Review;
import com.example.universalpetcare.model.User;
import com.example.universalpetcare.repository.AppointmentRepository;
import com.example.universalpetcare.repository.ReviewRepository;
import com.example.universalpetcare.repository.UserRepository;
import com.example.universalpetcare.request.ReviewUpdateRequest;
import com.example.universalpetcare.utils.FeedBackMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService{
    private final ReviewRepository reviewRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Override
    public Review saveReview(Review review, Long reviewerId, Long veterinarianId) {
        if (veterinarianId.equals(reviewerId)) {
            throw new IllegalArgumentException(FeedBackMessages.NOT_ALLOWED);
        }

        Optional<Review> exsistingReview = reviewRepository.findByVeterinarianIdAndPatientId(veterinarianId, reviewerId);
        if(exsistingReview.isPresent()) {
            throw new AlreadyExistsException(FeedBackMessages.ALREADY_REVIEWED);
        }

        boolean hadCompletedAppointments = appointmentRepository
                .existsByVeterinarianIdAndPatientIdAndStatus(veterinarianId, reviewerId, AppointmentStatus.COMPLETED);
        if(!hadCompletedAppointments) {
            throw new IllegalStateException(FeedBackMessages.CANNOT_REVIEW);
        }

        User vet = userRepository.findById(veterinarianId)
                .orElseThrow(() -> new ResourceNotFoundException(FeedBackMessages.VET_OR_PATIENT_NOT_FOUND));

        User patient = userRepository.findById(reviewerId)
                .orElseThrow(() -> new ResourceNotFoundException(FeedBackMessages.VET_OR_PATIENT_NOT_FOUND));

        review.setVeterinarian(vet);
        review.setPatient(patient);

        return reviewRepository.save(review);
    }

    @Transactional
    @Override
    public double getAverageRatingForVet(Long veterinarianId) {
        List<Review> reviews = reviewRepository.findByVeterinarianId(veterinarianId);
        return reviews.isEmpty() ? 0 : reviews.stream()
                .mapToInt(Review :: getStars)
                .average()
                .orElse(0.0);
    }

    @Override
    public Page<Review> findAllReviewsByUserId(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return reviewRepository.findAllUserById(userId, pageRequest);
    }


    @Override
    public Review updateReview(Long reviewerId, ReviewUpdateRequest updateRequest) {
        return reviewRepository.findById(reviewerId)
                .map(existingReview -> {
                    existingReview.setStars(updateRequest.getStars());
                    existingReview.setFeedback(updateRequest.getFeedback());
                    return reviewRepository.save(existingReview);
                }).orElseThrow(() -> new ResourceNotFoundException(FeedBackMessages.RESOURCE_NOT_FOUND));
    }

    @Override
    public void deleteReview(Long reviewerId) {
        reviewRepository.findById(reviewerId)
                .ifPresentOrElse(Review::removeRelationship, () -> {
                    throw new ResourceNotFoundException(FeedBackMessages.NOT_FOUND);
                });
        reviewRepository.deleteById(reviewerId);
    }
}
