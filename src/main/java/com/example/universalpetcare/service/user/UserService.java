package com.example.universalpetcare.service.user;

import com.example.universalpetcare.factory.UserFactory;
import com.example.universalpetcare.model.User;
import com.example.universalpetcare.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserFactory userFactory;
    public User add(RegistrationRequest request)
    {
        return userFactory.createUser(request);
    }
}
