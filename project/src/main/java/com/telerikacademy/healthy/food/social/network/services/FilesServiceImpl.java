package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.FileStorageException;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.CloudStorage;
import com.telerikacademy.healthy.food.social.network.services.contracts.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public class FilesServiceImpl implements FilesService {
    private final CloudStorage cloudStorage;

    @Autowired
    public FilesServiceImpl(CloudStorage cloudStorage) {
        this.cloudStorage = cloudStorage;
    }

    public String uploadFile(MultipartFile file, int width) {
        try {
            return cloudStorage.uploadFile(file.getBytes(), getFileType(file), width);
        } catch (IOException | NullPointerException e) {
            throw new FileStorageException(e.getMessage());
        }
    }

    @Override
    public String getFileType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType.substring(0, contentType.indexOf("/"));
    }
}

