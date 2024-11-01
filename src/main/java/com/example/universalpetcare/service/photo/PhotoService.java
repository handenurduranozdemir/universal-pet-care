package com.example.universalpetcare.service.photo;

import com.example.universalpetcare.exceptions.ResourceNotFoundException;
import com.example.universalpetcare.model.Photo;
import com.example.universalpetcare.model.User;
import com.example.universalpetcare.repository.PhotoRepository;
import com.example.universalpetcare.repository.UserRepository;
import com.example.universalpetcare.utils.FeedBackMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoService implements IPhotoService {
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    @Override
    public Photo savePhoto(MultipartFile file, Long userId)  throws IOException, SQLException
    {
        Optional<User> theUser = userRepository.findById(userId);
        Photo photo = new Photo();

        if(file != null && !file.isEmpty()) {
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            photo.setImage(photoBlob);
            photo.setFileType(file.getContentType());
            photo.setFileName(file.getOriginalFilename());
        }

        Photo savedPhoto = photoRepository.save(photo);
        theUser.ifPresent(user -> {
            user.setPhoto(savedPhoto);
        });
        userRepository.save(theUser.get());
        return savedPhoto;
    }


    @Override
    public Photo getPhotoById(Long photoId)
    {
        return photoRepository.findById(photoId)

                .orElseThrow(() -> new ResourceNotFoundException(FeedBackMessages.RESOURCE_NOT_FOUND));
    }

    @Override
    public Photo updatePhoto(Long id, MultipartFile file) throws SQLException, IOException {
        Photo photo = getPhotoById(id);
        if (photo != null) {
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            photo.setImage(photoBlob);
            photo.setFileType(file.getContentType());
            photo.setFileName(file.getOriginalFilename());
            return photoRepository.save(photo);
        }
        throw new ResourceNotFoundException(FeedBackMessages.NOT_FOUND);
    }

    @Transactional
    @Override
    public byte[] getImageData(Long id) throws SQLException {
        Photo photo = getPhotoById(id);

        if (photo != null) {
            Blob photoBlob = photo.getImage();
            int blobLength = (int) photoBlob.length();

            return new byte[blobLength];
        }
        return null;
    }

    @Transactional
    @Override
    public void deletePhoto(Long id, Long userId)
    {
        userRepository.findById(userId).ifPresentOrElse(User::removeUserPhoto, () -> {
            throw new ResourceNotFoundException(FeedBackMessages.NOT_FOUND);
        });

        photoRepository.findById(id)
                .ifPresentOrElse(photoRepository::delete, () -> {
                    throw new ResourceNotFoundException(FeedBackMessages.NOT_FOUND);
        });
    }
}
