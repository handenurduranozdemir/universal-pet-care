package com.example.universalpetcare.controller;

import com.example.universalpetcare.exceptions.ResourceNotFoundException;
import com.example.universalpetcare.model.Photo;
import com.example.universalpetcare.response.ApiResponse;
import com.example.universalpetcare.service.photo.IPhotoService;
import com.example.universalpetcare.utils.FeedBackMessages;
import com.example.universalpetcare.utils.UrlMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping (UrlMapping.PHOTOS)
@RequiredArgsConstructor
public class PhotoController {
    private final IPhotoService photoService;

    @PostMapping(UrlMapping.UPLOAD_PHOTO)
    public ResponseEntity<ApiResponse> uploadPhoto (@RequestParam("file") MultipartFile file,
                                                    @RequestParam("userId") Long userId)
    {
        try {
            Photo thePhoto = photoService.savePhoto(file, userId);

            return ResponseEntity.ok(new ApiResponse(FeedBackMessages.CREATE_SUCCESS,thePhoto.getId()));
        } catch (SQLException | IOException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(FeedBackMessages.SERVER_ERROR, null));
        }
    }

    @PutMapping(UrlMapping.UPDATE_PHOTO)
    public ResponseEntity<ApiResponse> updatePhoto(@PathVariable Long id, @RequestBody MultipartFile file)
    {
        try {
            Photo photo = photoService.updatePhoto(id, file);

            return ResponseEntity.ok(new ApiResponse(FeedBackMessages.UPDATE_SUCCESS, photo.getId()));
        } catch (ResourceNotFoundException | SQLException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(FeedBackMessages.RESOURCE_NOT_FOUND,null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(FeedBackMessages.SERVER_ERROR,null));
        }

    }

    @DeleteMapping(UrlMapping.DELETE_PHOTO)
    public ResponseEntity<ApiResponse> deletePhoto(@PathVariable Long id, @PathVariable Long userId)
    {
        try {
            Photo photo = photoService.getPhotoById(id);
            if (photo != null) {
                photoService.deletePhoto(id, userId);
                return ResponseEntity.ok(new ApiResponse(FeedBackMessages.DELETE_SUCCESS,null));
            }

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(FeedBackMessages.RESOURCE_NOT_FOUND,null));
        }

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(null, INTERNAL_SERVER_ERROR));
    }

    @GetMapping(UrlMapping.GET_PHOTO_BY_ID)
    public ResponseEntity<ApiResponse> getPhotoById (@PathVariable Long id)
    {
        try {
            Photo photo = photoService.getPhotoById(id);
            if (photo != null) {
                byte[] photoBytes =photoService.getImageData(photo.getId());
                return ResponseEntity.ok(new ApiResponse(FeedBackMessages.RESOURCE_FOUND,photoBytes));
            }

        } catch (ResourceNotFoundException | SQLException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(FeedBackMessages.RESOURCE_NOT_FOUND, null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(null, NOT_FOUND));
    }
}
