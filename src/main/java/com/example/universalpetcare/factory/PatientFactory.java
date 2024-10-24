package com.example.universalpetcare.factory;

import com.example.universalpetcare.model.Patient;
import com.example.universalpetcare.repository.PatientRepository;
import com.example.universalpetcare.request.RegistrationRequest;
import com.example.universalpetcare.service.user.UserAttributesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientFactory {
    private final PatientRepository patientRepository;
    private final UserAttributesMapper userAttributesMapper;
    public Patient createPatient(RegistrationRequest registrationRequest) {
        Patient patient = new Patient();
        userAttributesMapper.setCommonAttributes(registrationRequest, patient);
        return patientRepository.save(patient);
    }
}
