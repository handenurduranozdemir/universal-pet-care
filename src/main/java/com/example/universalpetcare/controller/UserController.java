package com.example.universalpetcare.controller;

import com.example.universalpetcare.dto.EntityConverter;
import com.example.universalpetcare.dto.UserDto;
import com.example.universalpetcare.exceptions.ResourceNotFoundException;
import com.example.universalpetcare.exceptions.AlreadyExistsException;
import com.example.universalpetcare.model.User;
import com.example.universalpetcare.request.RegistrationRequest;
import com.example.universalpetcare.request.UserUpdateRequest;
import com.example.universalpetcare.response.ApiResponse;
import com.example.universalpetcare.service.user.UserService;
import com.example.universalpetcare.utils.FeedBackMessages;
import com.example.universalpetcare.utils.UrlMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RequestMapping(UrlMapping.USERS)
@RestController
public class UserController {
    private final UserService userService;
    private final EntityConverter<User, UserDto> entityConverter;

    @PostMapping(UrlMapping.REGISTER_USER)
    public ResponseEntity<ApiResponse> register(@RequestBody RegistrationRequest request)
    {
        try {
            User theUser = userService.register(request);
            UserDto registeredUser = entityConverter.mapEntityToDto(theUser, UserDto.class);

            return  ResponseEntity.ok(new ApiResponse(FeedBackMessages.CREATE_SUCCESS, registeredUser));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping(UrlMapping.UPDATE_USER)
    public ResponseEntity<ApiResponse> update(@PathVariable Long userId, @RequestBody UserUpdateRequest request)
    {
        try{
            User theUser = userService.update(userId, request);
            UserDto updatedUser = entityConverter.mapEntityToDto(theUser, UserDto.class);

            return ResponseEntity.ok(new ApiResponse(FeedBackMessages.UPDATE_SUCCESS, updatedUser));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping(UrlMapping.GET_USER_BY_ID)
    public ResponseEntity<ApiResponse> findById(@PathVariable Long userId)
    {
      try {
         User user = userService.findById(userId);
         UserDto theUser = entityConverter.mapEntityToDto(user, UserDto.class);
         return ResponseEntity.status(FOUND).body(new ApiResponse(FeedBackMessages.RESOURCE_FOUND, theUser));
      } catch (ResourceNotFoundException e) {
          return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
      } catch (Exception e) {
          return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
      }
    }

    @DeleteMapping(UrlMapping.DELETE_USER_BY_ID)
    public ResponseEntity<ApiResponse> delete(@PathVariable Long userId)
    {
        try {
            userService.delete(userId);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessages.DELETE_SUCCESS, null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMapping.GET_ALL_USERS)
    public ResponseEntity<ApiResponse> getAllUsers()
    {
        List<UserDto> theUsers = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse(FeedBackMessages.RESOURCE_FOUND, theUsers));
    }
}
