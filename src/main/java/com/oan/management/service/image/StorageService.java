package com.oan.management.service.image;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

/**
 * Created by Oan on 15/03/2018.
 */
public interface StorageService {
    void init();

    void store(MultipartFile file, Long id);

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    void deleteById(Long id);

    boolean isCorrectImageType(MultipartFile file);
}
