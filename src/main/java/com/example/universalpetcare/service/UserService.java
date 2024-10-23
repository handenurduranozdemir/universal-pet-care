package com.example.universalpetcare.service;

import com.example.universalpetcare.model.User;
import com.example.universalpetcare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public void add(User user)
    {
        userRepository.save(user);
    }
}
