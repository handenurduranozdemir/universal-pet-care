package com.example.universalpetcare.dto;

import com.example.universalpetcare.enums.AppointmentStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class AppointmentDto {
    private Long id;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private LocalDate createdAt;
    private String Reason;
    private AppointmentStatus status;
    private String appointmentNo;
    private UserDto patient;
    private UserDto veterinarian;
    private List<PetDto> pets;
}
