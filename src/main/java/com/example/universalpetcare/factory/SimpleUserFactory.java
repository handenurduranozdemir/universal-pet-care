package com.example.universalpetcare.factory;

import com.example.universalpetcare.exceptions.UserAlreadyExistsException;
import com.example.universalpetcare.model.User;
import com.example.universalpetcare.repository.UserRepository;
import com.example.universalpetcare.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SimpleUserFactory implements UserFactory{
    private final UserRepository userRepository;
    private final VeterinarianFactory veterinarianFactory;
    private final PatientFactory patientFactory;
    private final AdminFactory adminFactory;

    @Override

    public User createUser(RegistrationRequest registrationRequest) {
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new UserAlreadyExistsException("Oops! " + registrationRequest.getEmail() + " already exist");
        }

        switch (registrationRequest.getUserType()) {
            case "VET"-> { return veterinarianFactory.createVeterinarian(registrationRequest); }
            case  "PATIENT" -> { return patientFactory.createPatient(registrationRequest); }
            case "ADMIN" -> { return adminFactory.createAdmin(registrationRequest); }
            default -> { return null; }
        }
    }
}
