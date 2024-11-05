package com.example.universalpetcare.service.appointment;

import com.example.universalpetcare.dto.AppointmentDto;
import com.example.universalpetcare.model.Appointment;
import com.example.universalpetcare.request.AppointmentUpdateRequest;
import com.example.universalpetcare.request.BookAppointmentRequest;

import java.util.List;

public interface IAppointmentService {
    Appointment createAppointment(BookAppointmentRequest request, Long sender, Long recipient);
    List<Appointment> getAllAppointments();
    Appointment updateAppointment(Long id, AppointmentUpdateRequest request);
    void deleteAppointment(Long id);
    Appointment getAppointmentById(Long id);
    Appointment getAppointmentByNo(String appointmentNo);
    List<AppointmentDto> getUserAppointments(Long userId);
}
