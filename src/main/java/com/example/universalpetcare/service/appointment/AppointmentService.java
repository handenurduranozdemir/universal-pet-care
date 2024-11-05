package com.example.universalpetcare.service.appointment;

import com.example.universalpetcare.dto.AppointmentDto;
import com.example.universalpetcare.dto.EntityConverter;
import com.example.universalpetcare.dto.PetDto;
import com.example.universalpetcare.enums.AppointmentStatus;
import com.example.universalpetcare.exceptions.ResourceNotFoundException;
import com.example.universalpetcare.model.Appointment;
import com.example.universalpetcare.model.Pet;
import com.example.universalpetcare.model.User;
import com.example.universalpetcare.repository.AppointmentRepository;
import com.example.universalpetcare.repository.UserRepository;
import com.example.universalpetcare.request.AppointmentUpdateRequest;
import com.example.universalpetcare.request.BookAppointmentRequest;
import com.example.universalpetcare.service.pet.IPetService;
import com.example.universalpetcare.utils.FeedBackMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService implements IAppointmentService{
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final IPetService petService;
    private final EntityConverter<Appointment,AppointmentDto> entityConverter;
    private final EntityConverter<Pet, PetDto> petEntityConverter;

    @Transactional
    @Override
    public Appointment createAppointment(BookAppointmentRequest request, Long senderId, Long recipientId) {
        Optional<User> sender = userRepository.findById(senderId);
        Optional<User> recipient = userRepository.findById(recipientId);

        if (sender.isPresent() && recipient.isPresent()) {

            Appointment appointment = request.getAppointment();
            List<Pet> pets = request.getPets();
            pets.forEach(pet -> pet.setAppointment(appointment));
            List<Pet> savedPets = petService.savePetForAppointment(pets);
            appointment.setPets(savedPets);

            appointment.addPatient(sender.get());
            appointment.addVeterinarian(recipient.get());
            appointment.setAppointmentNo();
            appointment.setStatus(AppointmentStatus.WAITING_FOR_APPROVAL);
            return appointmentRepository.save(appointment);
        }
        throw new ResourceNotFoundException(FeedBackMessages.SENDER_RECIPIENT_NOT_FOUND);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment updateAppointment(Long id, AppointmentUpdateRequest request) {
        Appointment existingAppointment = getAppointmentById(id);
        if(!Objects.equals(existingAppointment.getStatus(), AppointmentStatus.WAITING_FOR_APPROVAL)) {
            throw new IllegalStateException(FeedBackMessages.ALREADY_APPROVED);
        }
        existingAppointment.setAppointmentDate(LocalDate.parse(request.getAppointmentDate()));
        existingAppointment.setAppointmentTime(LocalTime.parse(request.getAppointmentTime()));
        existingAppointment.setReason(request.getReason());
        return appointmentRepository.save(existingAppointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        appointmentRepository.findById(id)
                .ifPresentOrElse(appointmentRepository::delete, () -> {
                    throw new ResourceNotFoundException(FeedBackMessages.NOT_FOUND);
                });

    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException(FeedBackMessages.NOT_FOUND));
    }

    @Override
    public Appointment getAppointmentByNo(String appointmentNo) {
        return appointmentRepository.findByAppointmentNo(appointmentNo);
    }

    @Override
    public List<AppointmentDto> getUserAppointments(Long userId)
    {
        List<Appointment> appointments = appointmentRepository.findAllByUserId(userId);

        return appointments.stream()
                .map(appointment -> {
                    AppointmentDto appointmentDto = entityConverter.mapEntityToDto(appointment, AppointmentDto.class);

                    List<PetDto> petDtos = appointment.getPets().stream()
                            .map(pet ->
                                petEntityConverter.mapEntityToDto(pet,PetDto.class)).toList();
                    appointmentDto.setPets(petDtos);
                    return appointmentDto;
                }).toList();
    }
}
