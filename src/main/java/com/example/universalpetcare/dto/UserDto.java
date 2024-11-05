package com.example.universalpetcare.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private String phoneNumber;
    private String email;
    private String password;
    private String userType;
    private boolean isEnable;
    private String specialization;
    private LocalDate createdAt;
    private List<AppointmentDto> appointments;
    private List<ReviewDto> reviews;
    private long imageId;
    private byte[] image;
    private double averageRating;
}
