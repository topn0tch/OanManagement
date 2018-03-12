package com.oan.management.service.image;

import com.oan.management.model.Image;
import com.oan.management.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Oan on 30/01/2018.
 */
public interface ImageService {
    Image uploadImage(MultipartFile file, String path, User user);
    List<Image> findAll();
    Image findById(Long id);
    Image findByTitle(String title);
    Image getUserImage(User user);
    Image removeUserImage(User user);
}
