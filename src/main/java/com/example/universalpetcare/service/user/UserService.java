package com.example.universalpetcare.service.user;

import com.example.universalpetcare.dto.EntityConverter;
import com.example.universalpetcare.dto.UserDto;
import com.example.universalpetcare.exceptions.ResourceNotFoundException;
import com.example.universalpetcare.factory.UserFactory;
import com.example.universalpetcare.model.User;
import com.example.universalpetcare.repository.UserRepository;
import com.example.universalpetcare.request.RegistrationRequest;
import com.example.universalpetcare.request.UserUpdateRequest;
import com.example.universalpetcare.utils.FeedBackMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserFactory userFactory;
    private final UserRepository userRepository;

    private final EntityConverter<User, UserDto> entityConverter;

    @Override
    public User register(RegistrationRequest request)
    {
        return userFactory.createUser(request);
    }

    @Override
    public User update(Long userId, UserUpdateRequest request)
    {
        User user = findById(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setSpecialization(request.getSpecialization());

        return userRepository.save(user);
    }

    @Override
    public User findById(Long userId)
    {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(FeedBackMessages.NOT_FOUND));
    }

    @Override
    public void delete(Long userId)
    {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository :: delete, () ->{
                    throw new ResourceNotFoundException(FeedBackMessages.NOT_FOUND);
                });
    }

    @Override
    public List<UserDto> getAllUsers()
    {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> entityConverter.mapEntityToDto(user, UserDto.class))
                .collect(Collectors.toList());
    }
}
