package com.example.universalpetcare.repository;

import com.example.universalpetcare.enums.AppointmentStatus;
import com.example.universalpetcare.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Appointment findByAppointmentNo(String appointmentNo);

    boolean existsByVeterinarianIdAndPatientIdAndStatus(Long veterinarianId, Long patientId, AppointmentStatus appointmentStatus);

    List<Appointment> findAllByUserId(Long userId);
}
