package com.example.universalpetcare.factory;

import com.example.universalpetcare.model.User;
import com.example.universalpetcare.request.RegistrationRequest;

public interface UserFactory {
    User createUser(RegistrationRequest registrationRequest);
}
