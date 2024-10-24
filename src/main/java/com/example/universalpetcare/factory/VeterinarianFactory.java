package com.example.universalpetcare.factory;

import com.example.universalpetcare.model.Veterinarian;
import com.example.universalpetcare.repository.VeterinarianRepository;
import com.example.universalpetcare.request.RegistrationRequest;
import com.example.universalpetcare.service.user.UserAttributesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VeterinarianFactory {
    private final VeterinarianRepository veterinarianRepository;
    private final UserAttributesMapper userAttributesMapper;

    public Veterinarian createVeterinarian(RegistrationRequest registrationRequest) {
        Veterinarian veterinarian = new Veterinarian();
        userAttributesMapper.setCommonAttributes(registrationRequest, veterinarian);
        veterinarian.setSpecialization(registrationRequest.getSpecialization());
        return veterinarianRepository.save(veterinarian);
    }
}
