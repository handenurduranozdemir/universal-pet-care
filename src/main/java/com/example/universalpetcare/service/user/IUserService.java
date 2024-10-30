package com.example.universalpetcare.service.user;

import com.example.universalpetcare.dto.UserDto;
import com.example.universalpetcare.model.User;
import com.example.universalpetcare.request.RegistrationRequest;
import com.example.universalpetcare.request.UserUpdateRequest;

import java.util.List;

public interface IUserService {
    User register(RegistrationRequest request);

    User update(Long userId, UserUpdateRequest request);

    User findById(Long userId);

    void delete(Long userId);

    List<UserDto> getAllUsers();
}
