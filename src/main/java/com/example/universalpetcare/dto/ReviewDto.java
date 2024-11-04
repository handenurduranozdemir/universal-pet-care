package com.example.universalpetcare.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private Long id;
    private String stars;
    private String feedback;
    private String reviewerFirstName;
    private String veterinarianFirstName;
}
