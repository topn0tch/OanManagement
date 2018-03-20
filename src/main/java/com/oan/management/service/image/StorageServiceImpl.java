package com.oan.management.service.image;

import com.oan.management.exception.StorageException;
import com.oan.management.exception.StorageFileNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

/**
 * @author Oan Stultjens
 * @since 15/03/2018
 * This is the implementation of {@link StorageService}
 * @see <a href="https://spring.io/guides/gs/uploading-files/">Spring.io - Uploading files</a><br/>
 * This one is edited to my own needs
 */
@Service
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation;

    @Autowired
    public StorageServiceImpl(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
        init();
    }

    /**
     * Stores a multipart file to the server
     * @throws StorageException
     * @param file {@link MultipartFile}
     * @param id Long for the filename which will be saved as {userid}.jpg
     */
    @Override
    public void store(MultipartFile file, Long id) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            if (ImageIO.read(file.getInputStream()) == null) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename()+ " - Not an image");
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(id.toString()+".jpg"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    /**
     * Loads a specified file as {@link Resource}
     * @throws StorageFileNotFoundException
     * @param filename String
     * @return Resource
     */
    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    /**
     * Deletes a profile picture by the specified id
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        Path path = load(id.toString()+".jpg");
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the uploaded {@link MultipartFile} is a correct image
     * Only '.png' and '.jpg' allowed
     * @param file {@link MultipartFile}
     * @return boolean
     */
    @Override
    public boolean isCorrectImageType(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
        if (extension != null && !extension.isEmpty()) {
            if (!extension.contains("png") && !extension.contains("jpg")) {
                return false;
            }
        }
        return true;
    }

    /**
     * The initializer for the storage.
     * @throws StorageException
     * @throws IOException
     */
    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (FileAlreadyExistsException e) {
            System.out.println("Directory already exists!");
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
